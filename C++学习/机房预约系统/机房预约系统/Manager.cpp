#include"Manager.h"

//默认构造
Manager::Manager() {

}
//有参构造
Manager::Manager(string name, string psw) {
	//初始化管理员信息
	this->m_Name = name;
	this->m_Pwd = psw;
	//初始化容器,获取老师和学生信息
	this->initVector();
	//初始化机房信息
	this->initComputer();
}
//添加账号
void Manager::addPerson() {
	cout << "请输入添加账号的类型：" << endl;
	cout << "1，添加学生" << endl;
	cout << "2，添加老师" << endl;

	string fileName;//操作文件名
	string tip;//提示id
	string errorTip;//重复错误提示

	ofstream ofs;
	
	int cmd = 0;
	cin >> cmd;
	if (cmd == 1) {//添加学生
		fileName = STUDENT_FILE;
		tip = "请输入学号：";
		errorTip = "学号重复，请重新输入（输入-1退出）";
	}
	else {//添加老师
		fileName = TEACHER_FILE;
		tip = "请输入职工号：";
		errorTip = "职工号重复，请重新输入（输入-1退出）";
	}
	ofs.open(fileName, ios::out | ios::app);

	int id;//学号/职工号
	string name;
	string pwd;

	cout << tip << endl;
	while (1) {
		cin >> id;
		if (id == -1) {
			ofs.close();
			system("cls");
			return;
		}
		if (this->checkRepeat(id, cmd)) {
			cout << errorTip << endl;
		}
		else {
			break;
		}
	}

	cout << "请输入姓名：" << endl;
	cin >> name;

	cout << "请输入密码：" << endl;
	cin >> pwd;

	//向文件中添加数据
	ofs << id << " " << name << " " << pwd << " " << endl;
	cout << "添加成功";

	system("pause");
	system("cls");

	ofs.close();
	
	//重新获取文件中数据
	this->initVector();
}
//清空预约记录
void Manager::clearPerson() {
	ofstream ofs(ORDER_FILE, ios::trunc);
	ofs.close();

	cout << "清空成功！" << endl;
	system("pause");
	system("cls");
}
//查看机房信息
void Manager::showComputer() {
	cout << "机房信息如下：" << endl;
	for (vector<ComputerRoom>::iterator it = vCom.begin(); it != vCom.end(); it++) {
		cout << "机房编号：" << it->m_ComId << " 最大容量：" << it->m_MaxNum << endl;
	}
	system("pause");
	system("cls");
}

void printStu(Student& s) {
	cout << "学号：" << s.m_Id << " 姓名：" << s.m_Name << " 密码：" << s.m_Pwd << endl;
}
void printTea(Teacher& t) {
	cout << "职工号：" << t.empId << " 姓名：" << t.m_Name << " 密码：" << t.m_Pwd << endl;
}
//查看账号
void Manager::showPerson() {
	cout << "选择查看的内容：" << endl;
	cout << "1，查看所有学生" << endl;
	cout << "2，查看所有老师" << endl;

	int cmd = 0;
	cin >> cmd;
	if (cmd == 1) {
		//查看学生
		for_each(vStu.begin(), vStu.end(), printStu);
	}
	else {
		//查看老师
		for_each(vTea.begin(), vTea.end(), printTea);
	}
	system("pause");
	system("cls");
}
//菜单界面
void Manager::operMenu() {
	cout << "欢迎管理员：" << this->m_Name << "登录！" << endl;
	cout << endl;
	cout << "1.添加账号" << endl;
	cout << "2.查看账号" << endl;
	cout << "3.查看机房" << endl;
	cout << "4.清空预约" << endl;
	cout << "0.注销登录" << endl;
	cout << endl;
	cout << "请输入您的选择：" << endl;
}
//初始化容器
void Manager::initVector() {
	//容器清空
	this->vStu.clear();
	this->vTea.clear();
	//读取信息
	ifstream ifs;
	ifs.open(STUDENT_FILE, ios::in);
	if (!ifs.is_open()) {
		cout << "文件读取失败！" << endl;
		return;
	}
	Student s;
	while (ifs >> s.m_Id && ifs >> s.m_Name && ifs >> s.m_Pwd) {
		vStu.push_back(s);
	}
	//cout << "学生数量：" << vStu.size() << endl;
	ifs.close();

	ifs.open(TEACHER_FILE, ios::in);
	if (!ifs.is_open()) {
		cout << "文件读取失败！" << endl;
		return;
	}
	Teacher t;
	while (ifs >> t.empId && ifs >> t.m_Name && ifs >> t.m_Pwd) {
		vTea.push_back(t);
	}
	//cout << "老师数量：" << vTea.size() << endl;
	ifs.close();
}
//检测重复
bool Manager::checkRepeat(int id/*检测学号/职工号*/, int type/*检测类型*/) {
	if (type == 1) {//检测学生
		for (vector<Student>::iterator it = vStu.begin(); it != vStu.end(); it++) {
			if (id == it->m_Id) {
				return 1;
			}
		}
	}
	else {//检测老师
		for (vector<Teacher>::iterator it = vTea.begin(); it != vTea.end(); it++) {
			if (id == it->empId) {
				return 1;
			}
		}
	}
	return 0;
}
//初始化机房
void Manager::initComputer() {
	ifstream ifs;
	ifs.open(COMPUTER_FILE, ios::in);
	if (!ifs.is_open()) {
		cout << "文件读取失败！" << endl;
		return;
	}
	ComputerRoom c;
	while (ifs >> c.m_ComId && ifs >> c.m_MaxNum) {
		this->vCom.push_back(c);
	}
	ifs.close();
	//cout << "机房数量：" << vCom.size();
}