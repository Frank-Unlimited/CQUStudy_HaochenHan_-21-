#include<iostream>
#include<string>
#include<vector>
using namespace std;

class User {
public:
	vector<User> vU;
	void addU() {
		User i;
		vU.push_back(i);
	}
};
int main() {
	User u;
}