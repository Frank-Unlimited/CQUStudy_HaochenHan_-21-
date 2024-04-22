#include<bits/stdc++.h>
using namespace std;

class Account{
	private:
		std::string id;
		double balance;
		static double total;	//��̬���ݳ�Ա�������˻����ܽ�� 
	protected:
		Account(const Data &date, const std::string &id);	//?
		void record(const Data &data, double amount, const std::string &desc);
		void error(const std::string &msg) const;
	public:
		const std::string &getId() const {return id;}
		double getBalance() const {return balance;}
		static double getTotal(){return total;}
		void show() const;
};

class CreditAccount : public Account{
	private:
		Accumulator acc;	//����������Ϣ���ۼ���
		double credit;		//���ö��
		double rate;		//Ƿ���������
		double fee;			//���ÿ����
		double getDebt() const;
	public:
		CreditAccount(const Date &date, const std::string &id, double credit, 
		double rate, double fee);
		double getCredit() const;
		double getRate() const;
		double getFee() const;
		double getAvailableCredit();
		//... 
};
