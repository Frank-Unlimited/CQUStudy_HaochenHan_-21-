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
	cout<<"1������ԤԼ"<<endl;
	cout<<"2���鿴�ҵ�ԤԼ"<<endl;
	cout<<"3���鿴����ԤԼ"<<endl;
	cout<<"4��ȡ��ԤԼ"<<endl;
	cout<<"5������"<<endl;
	cout<<"6���鿴�˻����"<<endl;
	cout<<"0��ע��"<<endl;
	cout<<endl;
	cout<<"����������ѡ��";
}

void User::applyOrder() {
	Court court;
	while(1) {
		cout<<"���ܳ���״̬��"<<endl;
		court.showCourt();
		cout<<"������ԤԼʱ��(����0����)��"<<endl;
		for(int i=1; i<=7; i++) cout<<i<<", ����"<<i<<endl;
		int cmd_date;
		cin>>cmd_date;
		if(cmd_date==0){
			system("cls");
			return;
		}
		if(cmd_date>=1&&cmd_date<=7) {
			cout<<"�����뿪ʼ�����ʱ�䣨0��23�㣩��";
			int cmd_time;
			cin>>cmd_time;
			if(cmd_time<0||cmd_time>23) {
				cout<<"�������"<<endl;
				continue;
			}

			cout<<"����"<<cmd_time<<"�㵽"<<cmd_time+1<<"����ೡ�ر��Ϊ��";
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
			cout<<"������ԤԼ���ر�ţ�";
			int cmd_court;
			cin>>cmd_court;
			bool flag=0;
			for(vector<int>::iterator it=freeCourt.begin(); it!=freeCourt.end(); it++) {
				if(cmd_court==*it) flag=1;
			}
			if(flag==0) {
				cout<<"�������"<<endl;
				continue;
			}

			court.court_status[cmd_date][cmd_court][cmd_time]=1;
			ofstream ofs("Order.txt",ios::out|ios::app);
			ofs<<cmd_date<<" "<<cmd_court<<" "<<cmd_time<<" "<<this->id<<" "<<this->name<<" 1"<<endl;
			cout<<"ԤԼ�ɹ��������"<<endl;
			court.updateFile();
			break;
		} else cout<<"������������������"<<endl;
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
