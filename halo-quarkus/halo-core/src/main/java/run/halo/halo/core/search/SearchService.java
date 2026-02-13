package run.halo.halo.core.search;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import run.halo.halo.core.entity.Post;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class SearchService {
    
    private static final Logger LOGGER = Logger.getLogger(SearchService.class.getName());
    
    @ConfigProperty(name = "halo.search.index-location")
    String indexLocation;
    
    private Directory directory;
    private StandardAnalyzer analyzer;
    
    public Uni<Void> init() {
        return Uni.createFrom().voidItem()
                .invoke(() -> {
                    directory = FSDirectory.open(Paths.get(indexLocation));
                    analyzer = new StandardAnalyzer();
                    LOGGER.info("Search index initialized at: " + indexLocation);
                });
    }
    
    public Uni<Void> indexPost(Post post) {
        return Uni.createFrom().voidItem()
                .invoke(() -> {
                    Document doc = new Document();
                    doc.add(new StringField("id", post.id.toString(), Field.Store.YES));
                    doc.add(new TextField("title", post.title, Field.Store.YES));
                    doc.add(new TextField("content", post.content, Field.Store.YES));
                    doc.add(new StringField("slug", post.slug, Field.Store.YES));
                    doc.add(new StringField("status", post.status.name(), Field.Store.YES));
                    
                    IndexWriterConfig config = new IndexWriterConfig(analyzer);
                    try (IndexWriter writer = new IndexWriter(directory, config)) {
                        writer.addDocument(doc);
                        writer.commit();
                    }
                })
                .onFailure().invoke(t -> LOGGER.warning("Failed to index post: " + t.getMessage()));
    }
    
    public Uni<Void> updatePost(Post post) {
        return deletePost(post.id).chain(() -> indexPost(post));
    }
    
    public Uni<Void> deletePost(Long postId) {
        return Uni.createFrom().voidItem()
                .invoke(() -> {
                    try {
                        IndexWriterConfig config = new IndexWriterConfig(analyzer);
                        try (IndexWriter writer = new IndexWriter(directory, config)) {
                            writer.deleteDocuments(new Term("id", postId.toString()));
                            writer.commit();
                        }
                    } catch (IOException e) {
                        LOGGER.warning("Failed to delete post from index: " + e.getMessage());
                    }
                });
    }
    
    public Uni<List<Long>> search(String queryStr, int limit) {
        return Uni.createFrom().item(() -> {
            try {
                QueryParser parser = new QueryParser("content", analyzer);
                Query query = parser.parse(queryStr);
                
                IndexReader reader = DirectoryReader.open(directory);
                IndexSearcher searcher = new IndexSearcher(reader);
                TopDocs topDocs = searcher.search(query, limit);
                
                List<Long> postIds = new ArrayList<>();
                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    postIds.add(Long.parseLong(doc.get("id")));
                }
                reader.close();
                
                return postIds;
            } catch (ParseException | IOException e) {
                LOGGER.warning("Search failed: " + e.getMessage());
                return new ArrayList<>();
            }
        });
    }
}
