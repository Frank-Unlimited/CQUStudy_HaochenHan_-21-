import pandas as pd
import numpy as np
from numpy import dot, log
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
import matplotlib.pyplot as plt

cost_x_list = []
cost_y_list = []


def sigmoid(z):
    return 1.0 / (1 + np.exp(-z))


def loss(h, y):  # 损失函数,h=w*x
    return -(y * log(h) + (1 - y) * log(1 - h)).mean()


def predict_prob(x, weight, b):  # 求得该类别概率
    return sigmoid(dot(x, weight) + b)


def predict(x, weight, b):  # 最终输出类别
    pre = sigmoid(dot(x, weight) + b)
    final = []
    for i in pre:
        if i >= 0.5:
            final.append(1)
        else:
            final.append(0)
    return np.array(final)  # final=0/1


def score(x, y, weight, b):  # 精确度
    pre = predict(x, weight, b)
    count = 0
    for index, result in enumerate(pre):
        if result == y[index]:
            count += 1
    # print(count)
    return float(count / len(y))


def fit(x, y, alpha, iterations):  # 拟合过程,alpha=学习率，iteration=最大迭代次数
    iteration = 0  # 迭代次数
    m, n = x.shape
    weight = np.zeros((n, 1))  # 初始权重
    b = 0  # 偏移量初始为0
    while 1:
        z = dot(x, weight) + b
        h = sigmoid(z)  # h即每次输出的结果
        j = loss(h, y)  # 进行损失计算
        # w,b的梯度求导过程可看我上面手写的那两幅图
        gradien_w = dot(x.T, h - y) / len(y)  # 以梯度推导结果求得梯度，.T为数组转置，不转置无法得内积
        gradien_b = np.mean(h - y)
        weight = weight - alpha * gradien_w  # 迭代
        b = b - alpha * gradien_b
        iteration += 1  # 迭代次数加一
        cost_x_list.append(iteration)
        cost_y_list.append(j)
        # if iteration % 500 == 0:  # 每500次打印一次损失
        #     print('loss:', j)
        if iteration == iterations:
            return weight, b, j  # 迭代完成，返回权重(b=常数项，j=损失)


def Watermelon_Huigui1():
    # 载入西瓜数据集
    # 密度
    density = np.array([0.697, 0.774, 0.634, 0.608, 0.556, 0.403, 0.481, 0.437, 0.666,
                        0.243, 0.245, 0.343, 0.639, 0.657, 0.360, 0.593, 0.719]).reshape(-1, 1)
    # 糖度
    sugar_rate = np.array([0.460, 0.376, 0.264, 0.318, 0.215, 0.237, 0.149, 0.211, 0.091,
                           0.267, 0.057, 0.099, 0.161, 0.198, 0.370, 0.042, 0.103]).reshape(-1, 1)

    x_tmp = np.hstack((density, sugar_rate))
    x = x_tmp
    # 标签
    y = np.array([1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                  0, 0, 0, 0, 0, 0]).reshape(-1, 1)

    # 逻辑对数回归
    w = fit(x, y, 0.1, 1000)
    print(w)
    l=w[2] #loss值
    # 结果可视化
    y = np.array([1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                  0, 0, 0, 0, 0, 0])
    zhenglei = []
    fulei = []
    for i in range(y.size):
        if (y[i] == 1):
            zhenglei.append(list(x_tmp[i, :]))
        else:
            fulei.append(list(x_tmp[i, :]))

    # 做出效果图
    zhenglei = np.array(zhenglei)
    zhenglei.reshape((-1, 2))
    fulei = np.array(fulei).reshape((-1, 2))
    plt.scatter(zhenglei[:, 0], zhenglei[:, 1], color='red', label="good watermelon")
    plt.scatter(fulei[:, 0], fulei[:, 1], color='blue', label="bad watermelon")
    xx = np.linspace(0, 100, 4)
    yy = (-w[0][0] * xx - w[1]) / w[0][1]
    plt.plot(xx, yy, color="black", linewidth=1.0, linestyle="-")
    plt.ylim(0, 1)
    plt.xlim(0, 1)
    plt.legend(loc='best')
    plt.show()
    b=w[1]
    w = np.array(w[0]).flatten()
    # 精度
    count = 0
    for i in range(y.size):
        if sigmoid(sum(w * x[i])+b) >= 0.5 and y[i] == 1:
            count = count + 1
        if (sigmoid(sum(w * x[i])+b) < 0.5 and y[i] == 0):
            count = count + 1
    print("精度：", count / y.size)

    # 查全率
    count = 0
    for i in range(y.size):
        if (sigmoid(sum(w * x[i])+b) > 0.5 and y[i] == 1):
            count = count + 1
    print("查全率：", count / zhenglei.size)

    # 查准率
    count = 0
    Cha_P = 0
    for i in range(y.size):
        if (sigmoid(sum(w * x[i])+b) > 0.5):
            Cha_P = Cha_P + 1
        if (sigmoid(sum(w * x[i])+b) > 0.5 and y[i] == 1):
            count = count + 1
    print("查准率：", count / Cha_P)
    print("loss：",l)

def Watermelon_Huigui2():
    df = pd.read_excel('w_data.xlsm')
    df1 = pd.read_excel('w_label.xlsm')
    # 将数据转换为 numpy 数组
    x = np.array(df)
    y = np.array(df1).reshape(-1,1)

    w=fit(x,y,0.1,1000)

    l = w[2]
    b = w[1]
    w = np.array(w[0]).flatten()

    count=0
    for i in range(y.size):
        if sigmoid(sum(w * x[i])+b) >= 0.5 and y[i] == 1:
            count = count + 1
        if sigmoid(sum(w * x[i])+b) < 0.5 and y[i] == 0:
            count = count + 1
    print("精度：", count / y.size)
    print("loss",l)

def iris_huigui():
    iris = load_iris()
    x = iris.data
    y = iris.target
    # 将数据集的 70%作为训练集,30%作为测试集,检验模型在测试集上的分类正确率
    x, x_test, y, y_test = train_test_split(x, y, test_size=0.3, random_state=0)
    # 训练第i种iris
    # 处理数据
    y = np.stack((y, y, y), axis=0)
    # 一对余分类器：修改label，属于第i类的标签设置为1，其余设置为0，用于MSE损失函数
    for i in range(3):
        for j in range(y[i].size):
            if (y[i, j] == i):
                y[i, j] = 1
            else:
                y[i, j] = 0

    w = np.zeros((3,x[0].size+1)) #记录权重
    for i in range(3):
        w_tmp = fit(x,np.array(y[i]).reshape(-1,1),0.01,50000)
        w[i] = np.append(np.array(w_tmp[0]).flatten(),w_tmp[1])

    print(w)

    res = np.zeros((len(x_test),1)) # 记录每个iris具体分为哪一类
    for i in range(len(x_test)):
        # sigmoid函数映射出的机率最大的对应类别为预测类别
        type=0
        MAX=0
        for j in range(3):
            z=sigmoid(sum(w[j,0:4]*x_test[i])+w[j,4])
            if z>MAX:
                MAX=z
                type=j
        res[i]=type

    res = np.array(res).flatten()


    count=0
    for i in range(len(y_test)):
        if(y_test[i]==res[i]):
            count+=1
    print("精度：",count/len(y_test))


Watermelon_Huigui2()
