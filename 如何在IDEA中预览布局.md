# 在 IntelliJ IDEA 中预览 Android 布局的方法

## 问题说明
IntelliJ IDEA 社区版（免费版）不包含 Android 布局可视化编辑器，
只能通过代码方式编辑 XML 布局文件。

## 解决方案

### 方案1：安装 Android Studio（推荐）

**Android Studio 是基于 IntelliJ IDEA 开发的 IDE，专为 Android 设计。**

优点：
- ✅ 完整的可视化布局编辑器
- ✅ Design + Code 双视图
- ✅ 拖拽组件
- ✅ 实时预览
- ✅ 设备适配预览

下载：https://developer.android.com/studio

---

### 方案2：继续使用 IDEA

如果坚持使用 IDEA，有以下几种预览方式：

#### 2.1 使用在线工具

1. **Android Layout Preview**
   - 网址：https://androidlayout.com/
   - 用法：复制 XML 粘贴即可预览

2. **Stetho**
   - Chrome 浏览器插件
   - 需要在应用中集成

#### 2.2 运行到模拟器预览

1. 启动 Android 模拟器
2. 运行应用
3. 实时查看布局效果

#### 2.3 使用 Layout Inspector（调试时）

1. 连接设备或模拟器
2. 运行应用
3. View → Tool Windows → Layout Inspector

---

### 方案3：升级到 IntelliJ IDEA Ultimate

付费版 Ultimate 包含更多 Android 支持，但仍不如 Android Studio。

---

## 推荐做法

**对于 Android 开发，强烈推荐使用 Android Studio！**

原因：
1. Google 官方出品
2. 基于 IntelliJ IDEA，界面熟悉
3. 完整的 Android 开发工具链
4. 更好的性能和稳定性
5. 及时的更新和支持

---

## 当前项目配置

✅ 项目配置正确（ProjectType = Android）
✅ ViewBinding 已启用
✅ 所有依赖正常

建议：下载 Android Studio 打开此项目即可看到完整预览。
