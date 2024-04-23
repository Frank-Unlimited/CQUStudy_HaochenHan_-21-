import numpy as np
from tqdm.std import trange
from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from tensorflow.examples.tutorials.mnist import input_data

class LinearLayer():  # 全连接层
    # mem: _W 权值，_b 偏置， _grad_W 权值梯度， _grad_b 偏置梯度，X本层输入
    def __init__(self, input_D, output_D=28):  # 初始化权值、偏置以及两者的梯度，input_D:输入节点个数，output_D输出节点个数
        self.mem = {}
        self.mem["_W"] = np.random.normal(0, 0.1, (input_D, output_D))  # 初始化不能为全0
        self.mem["_b"] = np.random.normal(0, 0.1, (1, output_D))
        self.mem["_grad_W"] = np.zeros((input_D, output_D))
        self.mem["_grad_b"] = np.zeros((1, output_D))

    def forward(self, X):  # 输入上一层的节点值，输出H(relu激活输入值)
        self.mem["X"] = X
        _W = self.mem["_W"]
        _b = self.mem["_b"]
        H = np.matmul(X, _W) + _b
        return H

    def backward(self, grad_H):  # 由上一层节点值和下一层的loss关于output的梯度共同决定本层梯度，输出给上一层的梯度
        X = self.mem["X"]
        _W = self.mem["_W"]
        self.mem["_grad_W"] = np.matmul(X.T, grad_H)
        self.mem["_grad_b"] = np.matmul(grad_H.T, np.ones(X.shape[0]))
        # print("hi",grad_H)
        # print(_W.T)
        grad_X = np.matmul(grad_H, _W.T)
        return grad_X

    def update(self, learn_rate):
        self.mem["_W"] = self.mem["_W"] - self.mem["_grad_W"] * learn_rate
        self.mem["_b"] = self.mem["_b"] - self.mem["_grad_b"] * learn_rate

    def Print(self):
        print(self.mem)

class Relu():
    # mem: X 输入，OUT 输出，grad 梯度
    def __init__(self):
        self.mem = {}

    def forward(self, X):
        self.mem["X"] = X
        self.mem["OUT"] = np.where(X < 0, np.zeros_like(X), X)
        return self.mem["OUT"]

    def backward(self, grad_H):
        X = self.mem["X"]
        self.mem["grad"] = (X > 0).astype(np.float32) * grad_H
        return self.mem["grad"]

    def Print(self):
        print(self.mem)


# 实现MSE损失函数
class MSEloss():
    # mem: predict_y 样本预测值，label_Y 样本真是标记，grad 梯度
    def __init__(self):
        self.mem = {}

    def forward(self, predict_y, label_y):
        self.mem["predict_y"] = predict_y
        self.mem["label_y"] = label_y
        loss = mse_loss(label_y,predict_y)
        return loss

    def backward(self):
        train_y = self.mem["label_y"]
        y = self.mem["predict_y"]
        self.mem["grad"] = (y - train_y).reshape(y.shape[0],len(train_y[0]))
        return self.mem["grad"]

    def Print(self):
        print(self.mem)


# 搭建全连接神经网络模型
class FullConnectionModel():
    # mem: input_dims 输入层维度，latent_dims隐藏层维度，output_dims 输出层维度，predict_y 样本预测值，loss loss值
    # 1个隐藏层，1个relu激活层，1个输出层，1个损失计算层
    def __init__(self, input_D, latent_dims, output_D):
        self.mem = {}
        self.Mul_H1 = LinearLayer(input_D, latent_dims)
        self.relu = Relu()
        self.Mul_H2 = LinearLayer(latent_dims, output_D)
        self.MSE_Loss = MSEloss()
        self.mem["input_dims"] = input_D
        self.mem["latent_dims"] = latent_dims
        self.mem["output_dims"] = output_D

    def forward(self, X, y_labels):
        o0 = X
        a1 = self.Mul_H1.forward(o0)
        o1 = self.relu.forward(a1)
        a2 = self.Mul_H2.forward(o1)
        o2 = a2
        # print("o2:",o2)
        # print(o2.shape)
        self.mem["predict_y"] = y = o2.reshape(y_labels.shape)
        self.mem["loss"] = self.MSE_Loss.forward(predict_y=y, label_y=y_labels)

    def backward(self):
        grad_loss_to_h2 = self.MSE_Loss.backward()
        grad_h2_to_relu = self.Mul_H2.backward(grad_loss_to_h2)
        grad_relu_to_h1 = self.relu.backward(grad_h2_to_relu)
        grad_X = self.Mul_H1.backward(grad_relu_to_h1)

    def upgrade(self, learn_rate):
        self.Mul_H1.update(learn_rate)
        self.Mul_H2.update(learn_rate)

    def Print(self):
        print("predict_y:",self.mem["predict_y"])
        print("loss:", self.mem["loss"])

# 编写MSE损失函数
def mse_loss(y_true, y_pred, size_average=True, reduce=True):
    # Compute the difference between the predicted values and the true values
    diff = y_pred - y_true

    # Compute the squared error for each sample
    sq_error = np.square(diff)

    # Compute the mean squared error across all samples
    if size_average and reduce:
        mse_loss = np.mean(sq_error)
    elif reduce:
        mse_loss = np.sum(sq_error)
    else:
        mse_loss = sq_error
    return mse_loss

# 计算精确度
def computeAccuracy(prob, labels):
    predicitions = np.argmax(prob, axis=1)
    truth = np.argmax(labels, axis=1)
    return np.mean(predicitions == truth)

# 训练一次模型
def trainOneStep(model, x_train, y_train, learning_rate=1e-5):
    model.forward(x_train, y_train)
    model.backward()
    model.upgrade(learning_rate)
    loss = model.mem["loss"]
    accuracy = computeAccuracy(model.mem["predict_y"], y_train)
    return loss, accuracy

# 模型寻优
def train(x_train, y_train, x_validation, y_validation,epochs = 10000, latent_dims_list = [100, 200, 300],learning_rate = 1e-5):
    best_accuracy = 0
    best_latent_dims = 0
    output_D = len(y_train[0])
    # 在验证集上寻优
    print("Start seaching the best parameter...\n")
    for latent_dims in latent_dims_list:
        model = FullConnectionModel(len(x_train[0]),latent_dims,output_D)

        bar = trange(20)  # 使用 tqdm 第三方库，调用 tqdm.std.trange 方法给循环加个进度条
        for epoch in bar:
            loss, accuracy = trainOneStep(model, x_train, y_train, learning_rate)
            bar.set_description(f'Parameter latent_dims={latent_dims: <3}, epoch={epoch + 1: <3}, loss={loss: <10.8}, accuracy={accuracy: <8.6}')  # 给进度条加个描述
        bar.close()

        validation_loss, validation_accuracy = evaluate(model, x_validation, y_validation)
        print(f"Parameter latent_dims={latent_dims: <3}, validation_loss={validation_loss}, validation_accuracy={validation_accuracy}.\n")

        if validation_accuracy > best_accuracy:
            best_accuracy = validation_accuracy
            best_latent_dims = latent_dims

    # 得到最好的参数组合，训练最好的模型
    print(f"The best parameter is {best_latent_dims}.\n")
    print("Start training the best model...")
    best_model = FullConnectionModel(len(x_train[0]),best_latent_dims,output_D)
    x = np.concatenate([x_train, x_validation], axis=0)
    y = np.concatenate([y_train, y_validation], axis=0)
    bar = trange(epochs)
    for epoch in bar:
        loss, accuracy = trainOneStep(best_model, x, y, learning_rate)
        bar.set_description(f'Training the best model, epoch={epoch + 1: <3}, loss={loss: <10.8}, accuracy={accuracy: <8.6}')  # 给进度条加个描述
    bar.close()

    return best_model


# 评估模型
def evaluate(model, x, y):
    model.forward(x, y)
    loss = model.mem["loss"]
    accuracy = computeAccuracy(model.mem["predict_y"], y)
    return loss, accuracy

def iris_train():
    iris = load_iris()
    x = iris.data
    y = iris.target
    tmp = []
    T = [[1, 0, 0], [0, 1, 0], [0, 0, 1]]
    for i in y:
        tmp.append(T[i])
    y = np.array(tmp)
    # 将数据集的 70%作为训练集,30%作为测试集,检验模型在测试集上的分类正确率
    x_train, x_validation, y_train, y_validation = train_test_split(x, y, test_size=0.3, random_state=0)
    model = train(x_train,y_train,x_validation,y_validation)
    loss, accuracy = evaluate(model, x, y)
    print(f'Evaluate the best model, test loss={loss:0<10.8}, accuracy={accuracy:0<8.6}.')



def Yi_huo_train():
    # 训练数据：经典的异或分类问题
    train_X = np.array([[0, 0], [0, 1], [1, 0], [1, 1]])
    train_y = np.array([0, 1, 1, 0]).reshape(4,1)

    # 初始化网络，总共2层，输入数据是2维，第一层3个节点，第二层1个节点作为输出层，激活函数使用Relu
    model = FullConnectionModel(2, 3, 1)

    # 训练网络
    for i in range(1):
        loss, accuracy = trainOneStep(model, train_X, train_y, 1e-4)

        # 判断学习是否完成
        if i % 200 == 0:
            pass
        if loss < 0.001:
            print("训练完成！ 第%d次迭代" % (i))
            break

    # 测试结果：
    model.forward(train_X, train_y)
    model.Print()
    print("y_label:", train_y)

def MNIST_train():
    mnist = input_data.read_data_sets("dataset/", one_hot=True)
    model = train(mnist.train.images, mnist.train.labels, mnist.validation.images, mnist.validation.labels,200)

    loss, accuracy = evaluate(model, mnist.test.images, mnist.test.labels)
    print(f'Evaluate the best model, test loss={loss:0<10.8}, accuracy={accuracy:0<8.6}.')

##########################################
Yi_huo_train()





