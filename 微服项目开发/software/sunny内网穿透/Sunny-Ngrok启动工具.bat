chcp 65001
@echo OFF
color 0a
Title Sunny-Ngrok启动工具 by Sunny
Mode con cols=109 lines=30
:START
ECHO.
Echo                  ==========================================================================
ECHO.
Echo                                         Sunny-Ngrok客户端启动工具
ECHO.
Echo                                         作者: Sunny QQ：327388905
ECHO.
Echo                                         官方QQ群：426298277（一号群） 455801231（二号群）
ECHO.
Echo                                         官网：www.ngrok.cc
ECHO.
Echo                                         作者博客：www.sunnyos.com
ECHO.
Echo                  ==========================================================================
Echo.
echo.
echo.
:TUNNEL
set /p startCmd=请输入启动命令，可在隧道管理处复制：
echo.
%startCmd%
PAUSE
goto TUNNEL