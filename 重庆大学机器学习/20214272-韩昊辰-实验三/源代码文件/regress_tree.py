from numpy import *
from sklearn.datasets import load_diabetes



# 加载数据集
from sklearn.model_selection import train_test_split


def load_data(fileName):
    dataset = []
    fr = open(fileName).readlines()
    for line in fr:
        curline = line.strip().split('\t')
        fltline = list(map(float, curline))  # 加上list就是完整功能了，不然只用map会生成内存地址
        dataset.append(fltline)
    dataset = array(dataset)  # 转化为array数组，便于计算
    return dataset


# 计算平均值，负责生成叶子节点，叶子节点根据公式就是求所在子区域的平均值
def regLeaf(dataset):
    return mean(dataset[:, -1])


# 计算损失函数中的总方差=方差x样本个数=每个点与估计的差（叶节点的均值）的平方和
def regErr(dataset):
    return var(dataset[:, -1]) * shape(dataset)[0]


# 根据特征索引和分裂点划分数据集为左右两部分
def splitDataset(dataset, index_of_feature, value):
    mat0 = dataset[nonzero(dataset[:, index_of_feature] <= value)[0], :]
    mat1 = dataset[nonzero(dataset[:, index_of_feature] > value)[0], :]
    return mat0, mat1


# 选择最佳特征和相应的划分点
def choose_best_feature(dataset, leafType=regLeaf, errType=regErr, ops=(1, 4)):
    min_error = ops[0]  # 最小下降损失
    min_samples = ops[1]  # 最小样本数
    if len(set(dataset[:, -1])) == 1:  # 数据集中都是同一个目标值
        return None, leafType(dataset)  # 返回最佳划分特征为None,切分点为 均值

    m, n = shape(dataset)  # 统计数据集的行和列
    Loss = errType(dataset)  # 损失函数
    bestLoss = inf  # 最优切分点的误差
    bestIndex = 0  # 最优特征索引
    bestValue = 0  # 最优切分点的值

    for featureIndex in range(n - 1):  # 遍历特征
        for value in set(dataset[:, featureIndex]):
            mat0, mat1 = splitDataset(dataset, featureIndex, value)
            if (shape(mat0)[0] < min_samples) or (shape(mat1)[0] < min_samples):  # 若两个子区域的叶子节点小于4个样本，则跳过此划分点
                continue
            curLoss = errType(mat0) + errType(mat1)
            if curLoss < bestLoss:
                bestLoss = curLoss
                bestIndex = featureIndex
                bestValue = value
    if Loss - bestLoss < min_error:  # 若切分之前的损失-切分之后的损失< min_error，那么就是损失减少不明显，停止切分
        return None, leafType(dataset)
    mat0, mat1 = splitDataset(dataset, featureIndex, value)

    if (shape(mat0)[0] < min_samples) or (shape(mat1)[0] < min_samples):
        return None, leafType(dataset)

    return bestIndex, bestValue


# 生成回归树
def generate_tree(dataset, leafType=regLeaf, errType=regErr, ops=(1, 4)):
    feature_index, value = choose_best_feature(dataset, leafType, errType, ops)  # 最优切分点划分

    if feature_index == None:
        return value

    reTree = {}
    reTree['spInd'] = feature_index  # 最优切分特征的索引
    reTree['spVal'] = value  # 最优切分点的值
    lSet, rSet = splitDataset(dataset, feature_index, value)
    reTree['left'] = generate_tree(lSet, leafType, errType, ops)
    reTree['right'] = generate_tree(rSet, leafType, errType, ops)
    return reTree


# -------剪枝处理--------
# 判断是叶子还是树
def isTree(obj):
    return (type(obj).__name__ == 'dict')


# 获取左右叶子的平均值
def getMean(reTree):
    if isTree(reTree['left']):
        reTree['left'] = getMean(reTree['left'])
    if isTree(reTree['right']):
        reTree['right'] = getMean(reTree['right'])
    return (reTree['left'] + reTree['right']) / 2.0


# 后剪枝
def postpruning(reTree, test_data):
    if shape(test_data)[0] == 0:
        print("判断测试集为空，执行过吗？")
        return getMean(reTree)
    if (isTree(reTree['left']) or isTree(reTree['right'])):
        lSet, rSet = splitDataset(test_data, reTree['spInd'], reTree['spVal'])
    if isTree(reTree['left']):
        reTree['left'] = postpruning(reTree['left'], lSet)
    if isTree(reTree['right']):
        reTree['right'] = postpruning(reTree['right'], rSet)
    if not isTree(reTree['left']) and not isTree(reTree['right']):
        lSet, rSet = splitDataset(test_data, reTree['spInd'], reTree['spVal'])
        errorNoMerge = sum(power(lSet[:, -1] - reTree['left'], 2)) + sum(
            power(rSet[:, -1] - reTree['right'], 2))  # 剪枝前的误差
        treeMean = (reTree['left'] + reTree['right']) / 2.0
        errorMerge = sum(power(test_data[:, -1] - treeMean, 2))  # 剪枝后的误差
        if errorMerge < errorNoMerge:
            return treeMean  # 剪枝后的节点值为原来左右叶子结点的均值
        else:
            return reTree  # 不剪枝
    else:
        return reTree


# 回归树预测一个样本
def reTree_predict_one_test(reTree, one_test_example):
    first_feature_index = reTree[list(reTree.keys())[0]]  # 获取第一个结点的特征索引
    feature_spVal = reTree[list(reTree.keys())[1]]  # 获取第一个结点的分裂点
    predict_val = 0.0

    if one_test_example[first_feature_index] <= feature_spVal:
        if type(reTree['left']).__name__ == 'dict':
            predict_val = reTree_predict_one_test(reTree['left'], one_test_example)
        else:
            predict_val = reTree['left']
    else:
        if type(reTree['right']).__name__ == 'dict':
            predict_val = reTree_predict_one_test(reTree['right'], one_test_example)
        else:
            predict_val = reTree['right']
    return predict_val


# 回归树预测所有测试数据结果
def reTree_predict(reTree, test_data):
    classLabel = []
    for one_test in test_data:
        classLabel.append(reTree_predict_one_test(reTree, one_test))
    return classLabel


# 处理数据集
def handle_input_data(data,target):
    dataset = []
    for i in range(len(data)):
        tmp = list(append(data[i], target[i]))
        dataset.append(tmp)
    return array(dataset)


# 计算MSE误差
def cul_r(predict_y,y):
    res = 0
    for i in range(len(y)):
        r_i = (y[i]-predict_y[i])**2
        res += r_i
    return res/(len(y)**2)

def diabetes_retree():
    diabetes = load_diabetes()
    data = diabetes.data
    target = diabetes.target
    x_train, x_test, y_train, y_test = train_test_split(data, target, test_size=0.2, random_state=666)
    myData = handle_input_data(x_train, y_train)
    testData = handle_input_data(x_test, y_test)
    reTree = generate_tree(myData)
    reTree_str = str(reTree)
    print("未剪枝之前的决策树：")
    print(reTree_str)
    classlist = reTree_predict(reTree,testData)
    print("平均误差为：", cul_r(classlist,y_test))

    print("剪枝之后的决策树：")
    postPrunTree = postpruning(reTree, testData)
    print(postPrunTree)
    classlist = reTree_predict(postPrunTree, testData)
    print("平均误差为：", cul_r(classlist,y_test))


myData = load_data('re_data.txt')
reTree = generate_tree(myData)
reTree_str = str(reTree)
print("未剪枝之前的决策树：")
print(reTree_str)
test_example = [0.8935465]
predict_val = reTree_predict_one_test(reTree, test_example)
print(predict_val)
print("剪枝之后的决策树：")
testData = load_data('re_data_test.txt')
postPrunTree = postpruning(reTree, testData)
print(postPrunTree)
predict_val = reTree_predict_one_test(postPrunTree, test_example)
print(predict_val)


