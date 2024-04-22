#include "User.h"

User::User() {

}

User::User(int id, string name, string pwd) {
	this->id = id;
	this->name = name;
	this->pwd = pwd;
}

void User::operMenu() {
	cout<<endl;
	cout<<"1，申请预约"<<endl;
	cout<<"2，查看我的预约"<<endl;
	cout<<"3，查看所有预约"<<endl;
	cout<<"4，取消预约"<<endl;
	cout<<"5，付款"<<endl;
	cout<<"6，查看账户余额"<<endl;
	cout<<"0，注销"<<endl;
	cout<<endl;
	cout<<"请输入您的选择：";
}

void User::applyOrder() {
	Court court;
	while(1) {
		cout<<"本周场地状态："<<endl;
		court.showCourt();
		cout<<"请输入预约时间(输入0返回)："<<endl;
		for(int i=1; i<=7; i++) cout<<i<<", 星期"<<i<<endl;
		int cmd_date;
		cin>>cmd_date;
		if(cmd_date==0){
			system("cls");
			return;
		}
		if(cmd_date>=1&&cmd_date<=7) {
			cout<<"请输入开始打球的时间（0—23点）：";
			int cmd_time;
			cin>>cmd_time;
			if(cmd_time<0||cmd_time>23) {
				cout<<"输入错误！"<<endl;
				continue;
			}

			cout<<"当日"<<cmd_time<<"点到"<<cmd_time+1<<"点空余场地编号为：";
			vector<int> freeCourt;
			for(int i=1; i<=6; i++) {
	//cout<<endl<<"=="<<court.court_status[cmd_date][i][cmd_time]<<"=="<<endl;
	//court.showCourt();
				int x = court.court_status[cmd_date][i][cmd_time];
				cout<<endl<<"=="<<x<<"=="<<endl;
				if(x==0) {
					cout<<i<<" ";
					freeCourt.push_back(i);
				}
			}
			cout<<endl;
			cout<<"请输入预约场地编号：";
			int cmd_court;
			cin>>cmd_court;
			bool flag=0;
			for(vector<int>::iterator it=freeCourt.begin(); it!=freeCourt.end(); it++) {
				if(cmd_court==*it) flag=1;
			}
			if(flag==0) {
				cout<<"输入错误！"<<endl;
				continue;
			}

			court.court_status[cmd_date][cmd_court][cmd_time]=1;
			ofstream ofs("Order.txt",ios::out|ios::app);
			ofs<<cmd_date<<" "<<cmd_court<<" "<<cmd_time<<" "<<this->id<<" "<<this->name<<" 1"<<endl;
			cout<<"预约成功！审核中"<<endl;
			court.updateFile();
			break;
		} else cout<<"输入有误！请重新输入"<<endl;
	}
	system("pause");
	system("cls");
}

void User::showMyOrder() {

}

void User::showAllOrder() {

}

void User::cancelOrder() {

}

void User::payMoney() {

}

void User::showMoney() {

}
