#include<bits/stdc++.h>
using namespace std;

//////////////////////////////////////////////////////////////////////////////////////////
//Matrix类定义 
class Matrix{
	public:
		Matrix(int size=2);
		~Matrix();
		void setMatrix (const double *values);	//矩阵赋初值
		void printMatrix() const;
		int getSize() const {return size;}
		
		double &element(int i, int j) {return elements[i*size+j];}
	private:
		int size;
		double *elements;	//矩阵存放数据的首地址 
};

///////////////////////////////////////////////////////////////////////////////////////////////
//LinearEqu 类定义
class LinearEqu:public Matrix{
	public:
		LinearEqu(int size=2);
		~LinearEqu();
		void setLinearEqu(const double*a, const double*b);	//方程赋值
		bool solve();
		void printLinearEqu() const;
		void printSolution() const;
	private:
		double*sums;	//方程右端项
		double*solutions;	//方程的解 
}; 

//////////////////////////////////////////////////////////////////////////////////////////
//Matrix类实现

void Matrix::setMatrix (const double *values){
	for(int i=0; i<size*size; i++) elements[i]=values[i];
}

Matrix::Matrix(int size/*=2*/):size(size){
	elements=new double[size*size];		//动态内存分配 
}

Matrix::~Matrix(){
	delete[] elements;		//内存释放 
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
//LinearEqu类实现
LinearEqu::LinearEqu(int size):Matrix(size){	//用size调用基类构造函数 
	sums = new double[size];
	solution = new double[size];
} 

LinearEqu::~LinearEqu(){
	delete[] sums;
	delete[] solution;
	//会自动调用基类析构函数 
}

void LinearEqu::setLinearEqu(const double*a, const double*b){		//设置线性方程组 
	setMatrix(a);	//调用基类函数
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

