# MoBoxFrp-Client

[![GitHub Release](https://img.shields.io/github/v/release/MossCG/MoBoxFrpClient)](https://github.com/MossCG/MoBoxFrpClient/releases)
[![Java Version](https://img.shields.io/badge/Java-23.2%25-blue)](https://openjdk.org/)

MoBoxFrp墨盒内网穿透Java网页+命令行客户端 <br>
（原MossFrp）

## ✨ 特性

- 🖥️ **命令行操作**：通过简洁的命令行界面管理隧道
- 📈 **网页端管理**：访问本地网页管理隧道
- 🔧 **易于配置**：支持直接获取穿透码生成配置
- 📊 **状态监控**：实时查看日志信息并管理隧道

## 📋 系统要求

- **Java 运行环境** (Java 8 或更高版本)
- **Maven** (仅用于构建项目)
- **支持的操作系统**：Windows / Linux / macOS(?)

## 🚀 快速开始

### 1. 安装方式

#### 下载预编译版本
前往 [Releases 页面](https://github.com/MossCG/MoBoxFrpClient/releases) 下载最新版本的 `MoBoxFrpClient.jar`。

### 2. 基本使用

```bash
# 1.双击运行
# 理论上支持双击运行，会自动打开命令行界面

# 2.命令行直接运行
java -jar MoBoxFrpClient.jar

# 3.在客户端文件夹下创建run.bat并输入以下内容保存运行：
@echo off
java -Xmx128m -server -jar MoBoxFrpClient.jar
pause
```
完成启动后，您可以通过网页访问
`http://127.0.0.1:11451/`
或命令行来管理配置及隧道