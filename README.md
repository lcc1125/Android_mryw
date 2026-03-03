# 每日一练 (Daily Practice) - Android刷题应用

## 项目概述

一款基于语音识别的简答题刷题应用，支持5000用户规模，包含题库管理、用户系统、学习统计三大核心功能。

## 技术栈

### Android客户端
- **语言**: Java
- **架构**: MVVM (Model-View-ViewModel)
- **UI**: Material Design 3
- **导航**: Android Navigation Component
- **本地数据库**: Room
- **网络请求**: Retrofit + OkHttp
- **语音识别**: Google Speech Recognition API (Android内置)
- **图表**: MPAndroidChart
- **依赖注入**: 手动DI (可扩展至Hilt/Dagger)

### 后端API
- **语言**: Java 17
- **框架**: Spring Boot 3.2.0
- **数据库**: PostgreSQL 15
- **ORM**: Spring Data JPA
- **认证**: JWT (JSON Web Token)
- **安全**: Spring Security

## 系统架构

```
┌─────────────────────┐         ┌─────────────────────┐
│   Android客户端     │◄───────►│   后端API服务       │
│   (Java + MVVM)     │  REST   │   (Spring Boot)    │
├─────────────────────┤         ├─────────────────────┤
│ - 语音识别(Google)  │         │ - 评分算法          │
│ - Material Design   │         │ - JWT认证           │
│ - Room本地缓存      │         │ - JPA + PostgreSQL  │
└─────────────────────┘         └──────────┬──────────┘
                                           │
                                     ┌─────▼─────┐
                                     │ PostgreSQL │
                                     │  数据库    │
                                     └───────────┘
```

## 核心功能

### 1. 用户认证
- 用户注册/登录
- JWT Token认证
- 自动登录保持

### 2. 题库管理
- 每日推荐题目
- 按难度筛选（简单/中等/困难）
- 按分类筛选
- 随机出题

### 3. 语音答题
- **语音识别**: 使用Android内置SpeechRecognizer
- **语音转文字**: 实时显示识别结果
- **文字输入**: 支持手动输入答案
- **语音权限**: 动态请求录音权限

### 4. 智能评分
**评分公式**:
```
总分 = (基础分20分 + 关键词得分) × 难度系数

关键词得分 = (匹配关键词权重之和 / 所有关键词权重之和) × 80分

难度系数：EASY=1.0, MEDIUM=1.0, HARD=1.1
```

**关键词匹配特性**:
- 支持同义词匹配
- 必答关键词检查
- 可配置权重

### 5. 学习统计
- 总答题数统计
- 正确率分析
- 连续学习天数
- 学习趋势图表
- 答题历史记录

### 6. 个人中心
- 用户信息展示
- 资料编辑
- 退出登录

## 数据库Schema

### 核心表结构

| 表名 | 主要字段 | 用途 |
|------|---------|------|
| **users** | id, username, password, email, nickname, avatar | 用户信息 |
| **questions** | id, content, type, difficulty, category_id, standard_answer | 题目库 |
| **keywords** | id, question_id, keyword, weight, is_required, synonym_keywords | 关键词（用于评分） |
| **answer_records** | id, user_id, question_id, user_answer, score, matched_keywords | 答题记录 |
| **learning_statistics** | id, user_id, date, total_questions, correct_count, consecutive_days | 学习统计 |

## 项目结构

### Android客户端
```
com.example.myapplication/
├── data/
│   ├── local/           # Room数据库 + SharedPreferences
│   │   ├── entity/      # 数据库实体
│   │   ├── dao/         # 数据访问对象
│   │   └── AppDatabase.java
│   ├── model/           # 数据模型
│   ├── remote/          # Retrofit API接口
│   └── repository/      # 数据仓库层
├── ui/
│   ├── auth/            # 登录/注册
│   ├── home/            # 首页（今日题目）
│   ├── practice/        # 答题页面 + 语音识别
│   ├── result/          # 答题结果
│   ├── statistics/      # 学习统计
│   └── profile/         # 个人中心
└── utils/               # 工具类
```

### 后端项目
```
com.dailypractice/
├── entity/              # JPA实体类
├── repository/          # 数据访问层
├── service/             # 业务逻辑层
│   ├── AuthService.java
│   └── GradingService.java  # 评分算法
├── controller/          # REST API控制器
├── config/              # 配置类
└── dto/                 # 数据传输对象
```

## 核心API接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/login` | POST | 用户登录 |
| `/api/questions/daily` | GET | 获取今日题目 |
| `/api/answers/submit` | POST | 提交答案并评分 |
| `/api/statistics/overview` | GET | 学习统计概览 |
| `/api/users/profile` | GET/PUT | 用户信息 |

## 快速开始

### 前置要求

1. **Android开发环境**:
   - Android Studio Arctic Fox或更高版本
   - JDK 17
   - Android SDK API 24+
   - 模拟器或真机（支持语音识别）

2. **后端环境**:
   - JDK 17
   - Maven 3.6+
   - PostgreSQL 15

### 安装步骤

#### 1. 克隆项目
```bash
git clone <repository-url>
cd MyApplication
```

#### 2. 配置后端
```bash
cd backend

# 创建数据库
createdb daily_practice

# 修改 src/main/resources/application.yml 中的数据库配置

# 运行后端
mvn spring-boot:run
```

后端将运行在 `http://localhost:8080/api`

#### 3. 配置Android客户端
```bash
cd ../app

# 修改 ApiClient.java 中的 BASE_URL
# 模拟器: "http://10.0.2.2:8080/api/"
# 真机: "http://YOUR_PC_IP:8080/api/"

# 构建并运行
./gradlew installDebug
```

#### 4. 运行应用
1. 在Android Studio中打开项目
2. 连接模拟器或真机
3. 点击Run按钮

## 主要功能演示

### 用户注册/登录
1. 启动应用，进入登录页面
2. 点击"立即注册"创建账号
3. 填写用户名、邮箱、密码、昵称
4. 注册成功后自动登录

### 答题流程
1. 首页查看今日推荐题目
2. 点击题目进入答题页面
3. **语音答题**: 点击"语音输入"按钮，说出答案
4. **文字答题**: 直接在输入框输入答案
5. 点击"提交答案"
6. 查看评分结果和匹配的关键词

### 查看统计
1. 点击底部导航"统计"标签
2. 查看总答题数、正确率、连续天数
3. 查看学习趋势图表
4. 查看最近答题记录

## 语音识别实现

使用Android内置的`SpeechRecognizer`:

```java
SpeechRecognitionHelper speechHelper = new SpeechRecognitionHelper(context);
speechHelper.setCallback(this);
speechHelper.startListening("zh-CN");  // 中文识别
```

**权限要求**:
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
```

## 性能优化

### Android端
- Room数据库本地缓存
- Retrofit请求缓存
- LiveData自动更新UI
- ViewBinding视图绑定

### 后端端
- JPA查询优化
- 数据库索引
- JWT无状态认证
- CORS跨域支持

## 安全特性

- 密码BCrypt加密存储
- JWT Token认证
- SQL注入防护（JPA参数化查询）
- HTTPS支持（生产环境）

## 扩展性

### 可扩展功能
- [ ] 社交分享
- [ ] 排行榜
- [ ] 学习提醒
- [ ] 离线答题
- [ ] 题目收藏
- [ ] 错题本
- [ ] 学习计划
- [ ] 多语言支持

## 故障排除

### 常见问题

1. **语音识别失败**
   - 检查麦克风权限
   - 确认Google语音服务可用
   - 尝试使用文字输入

2. **无法连接后端**
   - 检查BASE_URL配置
   - 确认后端服务运行中
   - 检查网络连接

3. **数据库连接失败**
   - 确认PostgreSQL运行中
   - 检查数据库配置
   - 验证数据库权限

## 已实现功能清单

### Android客户端 ✅
- [x] 用户注册/登录UI
- [x] 底部导航栏
- [x] 首页-今日题目展示
- [x] 答题页面-语音输入
- [x] 答题页面-文字输入
- [x] 答题结果展示
- [x] 学习统计页面
- [x] 个人中心页面
- [x] Room数据库缓存
- [x] Retrofit网络请求
- [x] Material Design 3 UI

### 后端API ✅
- [x] 用户认证API
- [x] 题目管理API
- [x] 答题提交API
- [x] 统计数据API
- [x] 关键词匹配评分算法
- [x] JWT Token认证
- [x] PostgreSQL数据库集成

## 许可证

本项目仅用于学习目的。

## 贡献

欢迎提交Issue和Pull Request！

## 联系方式

如有问题，请通过Issue联系。

---

**最后更新**: 2026年3月
