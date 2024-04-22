#include<bits/stdc++.h>
using namespace std;

//////////////////////////////////////////////////////////////////////////////////////////
//Matrix�ඨ�� 
class Matrix{
	public:
		Matrix(int size=2);
		~Matrix();
		void setMatrix (const double *values);	//���󸳳�ֵ
		void printMatrix() const;
		int getSize() const {return size;}
		
		double &element(int i, int j) {return elements[i*size+j];}
	private:
		int size;
		double *elements;	//���������ݵ��׵�ַ 
};

///////////////////////////////////////////////////////////////////////////////////////////////
//LinearEqu �ඨ��
class LinearEqu:public Matrix{
	public:
		LinearEqu(int size=2);
		~LinearEqu();
		void setLinearEqu(const double*a, const double*b);	//���̸�ֵ
		bool solve();
		void printLinearEqu() const;
		void printSolution() const;
	private:
		double*sums;	//�����Ҷ���
		double*solutions;	//���̵Ľ� 
}; 

//////////////////////////////////////////////////////////////////////////////////////////
//Matrix��ʵ��

void Matrix::setMatrix (const double *values){
	for(int i=0; i<size*size; i++) elements[i]=values[i];
}

Matrix::Matrix(int size/*=2*/):size(size){
	elements=new double[size*size];		//��̬�ڴ���� 
}

Matrix::~Matrix(){
	delete[] elements;		//�ڴ��ͷ� 
}

void Matrix::printMatrix() const{
	cout<<"The Matrix is:"<<endl;
	for(int i=0; i<size; i++){
		for(int j=0; j<size; j++){
			cout<<element(i,j)<<",";
		cout<<endl;
		}
	}
}

////////////////////////////////////////////////////////////////////////////////////////
//LinearEqu��ʵ��
LinearEqu::LinearEqu(int size):Matrix(size){	//��size���û��๹�캯�� 
	sums = new double[size];
	solution = new double[size];
} 

LinearEqu::~LinearEqu(){
	delete[] sums;
	delete[] solution;
	//���Զ����û����������� 
}

void LinearEqu::setLinearEqu(const double*a, const double*b){		//�������Է����� 
	setMatrix(a);	//���û��ຯ��
	for(int i=0; i<getSize(); i++){
		sums[i] = b[i];
	} 
}

void LinearEqu::printLinearEqu() const{
	cout<<"The Line eqution is:"<<endl;
	for(int i=0;i<getSize();i++){
		for(j=0;j<size;j++) cout<<element(i,j)<<",";
	cout<<"		"<<sums[i]<<endl;
	}
}

void LinearEqu::printSolution() const{
	cout<<"The Reseult is:"<<endl;
	for(int i=0;i<getSize();i++){
		cout<<"x["<<i<<"]="<<solution[i]<<endl;
	}
}

inline void swap(double &v1, double &v2){
	double temp = v1;
	v1=v2;
	v2=temp;
}

bool LinearEqu::solve(){
	
}

