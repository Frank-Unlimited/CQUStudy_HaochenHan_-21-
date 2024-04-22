import cv2
import numpy as np
# 读取两张图片
img1 = cv2.imread('pic_c_2.png')
img2 = cv2.imread('pic_c_1.png')
# 将图片转换为灰度图像
gray1 = cv2.cvtColor(img1, cv2.COLOR_BGR2GRAY)
gray2 = cv2.cvtColor(img2, cv2.COLOR_BGR2GRAY)
# 计算两张图像的差异
diff = cv2.absdiff(gray1, gray2)
# 对差异图像进行二值化处理
_, thresh = cv2.threshold(diff, 50, 255, cv2.THRESH_BINARY)
# 寻找差异区域的轮廓
contours, _ = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
# 遍历轮廓，绘制差异区域的矩形框
for cnt in contours:
    x, y, w, h = cv2.boundingRect(cnt)
    cv2.rectangle(img1, (x, y), (x + w, y + h), (0, 0, 255), 2)
# 显示结果
cv2.imshow('result', img1)
cv2.waitKey(0)
cv2.destroyAllWindows()