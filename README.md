# BuilderCore

## 概述

BuilderCore 是由阿尔萨斯技术部开发的一款更适合建筑师使用的基础插件，可以在建筑服用来替代EssentialsX、CMI、BuilderUtilities并增加了一些建筑常用功能，插件还在持续更新中

## 特性

- **增强建筑工具**：提供高级建筑技巧的命令，包括无碰撞、夜视和穿墙等操作。
- **家传送点管理**：允许玩家设置、删除和传送到家和传送点。
- **warp点管理**：基于世界的warp点管理，根据世界区分warp点
- **传送**：包括传送、传送请求（TPA）和传送历史记录的命令。
- **生物放置**：下蹲使用怪物蛋右键即可放置无AI生物，空手右键还可以切换猫、狐狸、马的颜色和种类
- **实用命令**：提供物品编辑、玩家信息等实用命令。
- **AFK 管理**：在可配置的时间后自动检测并标记玩家为AFK。
- **基于权限的访问控制**：通过权限节点控制谁可以使用哪些命令和功能。

## 安装方式

1. 从[发布页面](https://github.com/Alsace-Technology-Department/BuilderCore/releases)下载最新的BuilderCore插件。
2. 将下载的`.jar`文件放入服务器的`plugins`目录下。
3. 重启服务器以加载插件。
4. 可选，根据需要编辑`config.yml`文件来配置插件。

## 权限节点

权限控制了对插件内各种命令和功能的访问。以下是一些示例：

- `buildercore.afk.bypass`：绕过自动AFK检测。
- `buildercore.commands.fly`：使用飞行命令。
- `buildercore.admin`：访问主插件命令和管理功能。

## 命令列表

BuilderCore提供了一系列用于建筑、传送、实用工具等的命令。示例包括：

- `/fly [player]`：为自己或其他玩家切换飞行模式。
- `/sethome <name>`：在当前位置设置一个家。
- `/warp <name>`：传送到预定义的传送点。

## 配置文件（`config.yml`）

`config.yml`文件允许服务器管理员自定义插件的行为。关键设置包括：

```yaml
#可以返回的位置
backHistory: 5
afk:
  threshold: 300 #自动afk时间
  placeholder: "[AFK]" #papi变量
```

[查看WIKI]()
