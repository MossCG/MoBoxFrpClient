package org.moboxlab.MoBoxFrpClient.Web;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import org.moboxlab.MoBoxFrpClient.BasicInfo;
import org.moboxlab.MoBoxFrpClient.Web.API.*;
import org.moboxlab.MoBoxFrpClient.Web.Page.*;
import org.moboxlab.MoBoxFrpClient.Cache.CacheSidebar;
import org.mossmc.mosscg.MossLib.File.FileCheck;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class WebMain {
    public static HttpServer server;
    public static HttpServerProvider provider;
    public static void initWeb() {
        try {
            //动态侧边栏模块初始化
            BasicInfo.logger.sendInfo("正在初始化侧边栏数据......");
            CacheSidebar.loadSidebar();

            //文件检查
            FileCheck.checkDirExist("./MoBoxFrp/pages");
            FileCheck.checkFileExist("./MoBoxFrp/pages/index.html","pages/index.html");
            FileCheck.checkFileExist("./MoBoxFrp/pages/codes.html","pages/codes.html");
            FileCheck.checkFileExist("./MoBoxFrp/pages/configCreate.html","pages/configCreate.html");
            FileCheck.checkFileExist("./MoBoxFrp/pages/configManager.html","pages/configManager.html");
            FileCheck.checkFileExist("./MoBoxFrp/pages/tunnel.html","pages/tunnel.html");

            //初始化接口
            provider = HttpServerProvider.provider();
            server = provider.createHttpServer(new InetSocketAddress(BasicInfo.config.getInteger("httpPort")),0);

            //页面部分
            server.createContext("/",new WebHandler());
            server.createContext("/services/codes",new PageCodes());
            server.createContext("/services/configCreate",new PageConfigCreate());
            server.createContext("/services/configManager",new PageConfigManager());
            server.createContext("/services/tunnel",new PageTunnel());


            //API接口部分
            //基本接口
            server.createContext("/API/Login",new APILogin());
            server.createContext("/API/UserInfo",new APIUserInfo());
            server.createContext("/API/ClientInfo",new APIClientInfo());
            server.createContext("/API/Codes",new APICodes());
            //配置文件接口
            server.createContext("/API/Config/Get",new APIGetConfig());
            server.createContext("/API/Config/Create",new APICreateConfig());
            server.createContext("/API/Config/Delete",new APIDeleteConfig());
            server.createContext("/API/Config/Edit",new APIEditConfig());
            //隧道操作接口
            server.createContext("/API/Tunnel/Start",new APIStartTunnel());
            server.createContext("/API/Tunnel/Stop",new APIStopTunnel());
            server.createContext("/API/Tunnel/Get",new APIGetTunnel());
            server.createContext("/API/Tunnel/Log",new APIGetTunnelLog());
            //信息类接口
            server.createContext("/API/Sidebar",new APISidebar());

            //启动服务
            server.setExecutor(Executors.newCachedThreadPool());
            server.start();
            BasicInfo.logger.sendInfo("Web页面已启动于本地端口"+BasicInfo.config.getInteger("httpPort")+"！");
        } catch (Exception e) {
            BasicInfo.logger.sendException(e);
        }

    }
}
