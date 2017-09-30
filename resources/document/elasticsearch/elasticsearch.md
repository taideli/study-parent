安装

解压后，要创建相应data、log目录，并且创建普通帐户 useradd es
把es根目录，数据目录，日志目录 修改所有者 chown -r es elasticsearch xx_data xx_log
修改config下的配置文件，指定host datadir logdir

修改/etc/security/limits.conf，添加如下内容(可能要重启才能生效，还没找到立刻生效的方法)
*       soft    nofile  65536
*       hard    nofile  131072
*       soft    nproc   2048
*       hard    nproc   4096

vim /etc/sysctl.conf
添加vm.max_map_count=655360
再应用 sysctl -p

切换到es用户，调用bin下的启动脚本即可


可以像zookeeper 一样配置单机多节点集群


一份教程
http://blog.csdn.net/cnweike/article/details/33736429