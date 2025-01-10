![img](file:///C:\Users\Lenovo\Documents\Tencent Files\2213412717\nt_qq\nt_data\Pic\2025-01\Ori\bcf3ec45af34846646dfb553a9674841.png)

修改wrapper.conf的jdk路径

![img](file:///C:\Users\Lenovo\Documents\Tencent Files\2213412717\nt_qq\nt_data\Pic\2025-01\Ori\127782051adbbac037862cb0db13f173.png)

在 schema.xml 中定义连接的数据库实例

![img](file:///C:\Users\Lenovo\Documents\Tencent Files\2213412717\nt_qq\nt_data\Pic\2025-01\Ori\214225897eaec36820cfed57d89d6596.png)

创建三个数据库，在每个数据库中建tb_test表，并插入一些数据

![img](file:///C:\Users\Lenovo\Documents\Tencent Files\2213412717\nt_qq\nt_data\Pic\2025-01\Ori\589078a45debb3a4c8f00819b60d4ec2.png)

在/bin下执行startup_nowrap.bat，在8066端口访问mysql，对TESTDB表的操作会分库分表到mycat123这三个数据库（例如select * from tb_test会得到三个数据库中表中所有的数据）