#include<bits/stdc++.h>
using namespace std;

class Account{
	private:
		std::string id;
		double balance;
		static double total;	//静态数据成员，所有账户的总金额 
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
		Accumulator acc;	//辅助计算利息的累加器
		double credit;		//信用额度
		double rate;		//欠款的日利率
		double fee;			//信用卡年费
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
