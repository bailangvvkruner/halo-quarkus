# GitHub Actions Secrets 配置

在 GitHub 仓库的 Settings -> Secrets and variables -> Actions 中配置以下 secrets：

## DockerHub Secrets

| Name | Description | Example |
|------|-------------|---------|
| `DOCKERHUB_USERNAME` | DockerHub 用户名 | `bailangvvkruner` |
| `DOCKERHUB_TOKEN` | DockerHub 访问令牌 | `dckr_pat_xxxxxx` |

### 获取 DockerHub Token

1. 访问 https://hub.docker.com/settings/security
2. 点击 "New Access Token"
3. 设置 Token 描述，如 "GitHub Actions"
4. 权限选择 "Read & Write"
5. 点击 "Generate"
6. 复制生成的 token，添加到 GitHub secrets

## GitHub Token

无需手动配置，GitHub 自动提供 `GITHUB_TOKEN`。

## 工作流说明

### docker-build.yml

**触发条件**:
- Push to main 或 v* 分支
- Pull Request 到 main 分支
- 手动触发

**步骤**:
1. Checkout 代码
2. 设置 JDK 21
3. Maven 编译
4. 验证构建产物
5. 设置 Docker Buildx
6. 登录 DockerHub（仅在非 PR 时）
7. 构建并推送 Docker 镜像
8. 生成构建摘要

**产物**:
- 多平台 Docker 镜像 (linux/amd64, linux/arm64)
- 标签: `latest`, `v1.0.0`, `main`, 等

### test.yml

**触发条件**:
- Push to main 或 develop 分支
- Pull Request 到 main 或 develop 分支

**步骤**:
1. Checkout 代码
2. 设置 JDK 21
3. 运行单元测试
4. 上传测试结果
5. 生成测试覆盖率报告
6. 上传覆盖率到 Codecov

### code-quality.yml

**触发条件**:
- Push to main 或 develop 分支
- Pull Request 到 main 或 develop 分支

**检查项**:
- Checkstyle
- SpotBugs
- PMD

### release.yml

**触发条件**:
- Push 标签 v* (如 v1.0.0)

**步骤**:
1. Checkout 代码
2. 设置 JDK 21
3. Maven 编译
4. 创建 Release 产物
5. 生成 Release Notes
6. 创建 GitHub Release

**产物**:
- halo-quarkus-{version}.jar
- halo-quarkus-latest.jar
- halo-quarkus-{version}.zip
- Docker 镜像推送
