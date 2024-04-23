import pandas as pd
import numpy as np

##将数据转化为（属性值：数据）的元组形式返回，并删除之前的特征列
def drop_exist_feature(data, best_feature):
    attr = pd.unique(data[best_feature])
    new_data = [(nd, data[data[best_feature] == nd]) for nd in attr]
    new_data = [(n[0], n[1].drop([best_feature], axis=1)) for n in new_data]
    return new_data
# 离散型
def drop_exist_feature_lisan(data, best_feature, value, type):
    attr = pd.unique(data[best_feature]) #表示特征所有取值的数组
    if type == 1: #使用特征==value的值进行划分
        new_data = [[value], data.loc[data[best_feature] == value]]
    else:
        new_data = [attr, data.loc[data[best_feature] != value]]
    new_data[1] = new_data[1].drop([best_feature], axis=1) #删除该特征
    return new_data

# 预测单条数据
def predict(Tree , test_data):
    first_feature = list(Tree.keys())[0]
    second_dict = Tree[first_feature]
    input_first = test_data.get(first_feature)
    input_value = second_dict[input_first]
    if isinstance(input_value , dict): #判断分支还是不是字典
        class_label = predict(input_value, test_data)
    else:
        class_label = input_value
    return class_label

#测试很多案例，话返回准确率
def predict_more(Tree, test_data, test_label):
    cnt = 0
    #计算如果该节点不剪枝的准确率
    for i in range(len(test_data)):
        after_data = test_data.reset_index().loc[i].to_dict()
        pred = predict(Tree,  after_data)
        if pred == test_label[i]:
            cnt += 1
    return cnt / len(test_label)

#用于预测节点剪枝后的预测正确数
def equalNums(label, featPreLabel):
    res = 0
    for l in label:
        if l == featPreLabel:
            res += 1
    return res

# 后剪枝
def post_prunning(tree , test_data , test_label , names):
    newTree = tree.copy() #copy是浅拷贝
    names = np.asarray(names)
    # 取决策节点的名称 即特征的名称
    featName = list(tree.keys())[0]
    # 取特征的列
    featCol = np.argwhere(names == featName)[0][0]
    names = np.delete(names, [featCol]) #删掉使用过的特征
    newTree[featName] = tree[featName].copy() #取值
    featValueDict = newTree[featName] #当前特征下面的取值情况
    print(featValueDict)
    featPreLabel = featValueDict.pop("prun_label") #如果当前节点剪枝的话是什么标签，并删除_vpdl
    print(featValueDict)
    # 分割测试数据 如果有数据 则进行测试或递归调用:
    split_data = drop_exist_feature(test_data,featName) #删除该特征，按照该特征的取值重新划分数据
    split_data = dict(split_data)

    for featValue in featValueDict.keys(): #每个特征的值
        if type(featValueDict[featValue]) == dict: #如果下一层还是字典，说明还是子树

            split_data_feature = split_data[featValue] #特征某个取值的数据，如“脐部”特征值为“凹陷”的数据
            split_data_lable = split_data[featValue].iloc[:, -1].values
            # 递归到下一个节点
            newTree[featName][featValue] = post_prunning(featValueDict[featValue],split_data_feature,split_data_lable,split_data_feature.columns)

    # 根据准确率判断是否剪枝，注意这里的准确率是到达该节点数据预测正确的准确率，而不是整体数据集的准确率
    # 因为在修改当前节点时，走到其他节点的数据的预测结果是不变的，所以只需要计算走到当前节点的数据预测对了没有即可
    ratioPreDivision = equalNums(test_label, featPreLabel) / test_label.size #判断测试集的数据如果剪枝的准确率

    #计算如果该节点不剪枝的准确率
    ratioAfterDivision = predict_more(newTree, test_data, test_label)

    if ratioAfterDivision < ratioPreDivision:
        newTree = featPreLabel # 返回剪枝结果，其实也就是走到当前节点的数据最多的那一类

    return newTree

if __name__ == '__main__':
    #读取数据
    train_data = pd.read_csv('./train_data.csv')
    test_data = pd.read_csv('./test_data.csv')
    test_data_label = test_data.iloc[:, -1].values
    names = test_data.columns

    dicision_Tree = {"脐部": {"prun_label": 1
                                   , '凹陷': {'色泽':{"prun_label": 1, '青绿': 1, '乌黑': 1, '浅白': 0}}
                                   , '稍凹': {'根蒂':{"prun_label": 1
                                                  , '稍蜷': {'色泽': {"prun_label": 1
                                                                  , '青绿': 1
                                                                  , '乌黑': {'纹理': {"prun_label": 1
                                                                               , '稍糊': 1, '清晰': 0, '模糊': 1}}
                                                                  , '浅白': 1}}
                                                  , '蜷缩': 0
                                                  , '硬挺': 1}}
                                   , '平坦': 0}}
    print('剪枝前的决策树:')
    print(dicision_Tree)
    print('剪枝前的测试集准确率: {}'.format(predict_more(dicision_Tree, test_data, test_data_label)))

    print('-'*20  + '剪枝' + '-'*20)
    new_tree = post_prunning(dicision_Tree,test_data , test_data_label , names)
    print('剪枝后的决策树:')
    print(new_tree)
    print('剪枝后的测试集准确率: {}'.format(predict_more(new_tree, test_data, test_data_label)))