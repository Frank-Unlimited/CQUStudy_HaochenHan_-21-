import cv2
import numpy as np
# 设置阈值
threshold = 50
# 读取视频
cap = cv2.VideoCapture('obstacle_video.mp4')
# 遍历视频帧
while True:
    # 读取视频中的一帧
    ret, frame = cap.read()
    if ret == False:
        break
    # 转换为灰度图像
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    # 二值化处理
    _, thresh = cv2.threshold(gray, threshold, 255, cv2.THRESH_BINARY)
    # 寻找轮廓
    contours, _ = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    # 遍历轮廓
    for cnt in contours:
        area = cv2.contourArea(cnt)
        # 如果轮廓面积小于阈值，判断为障碍物
        if area < threshold:
            x, y, w, h = cv2.boundingRect(cnt)
            cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 0, 255), 2)
    # 显示结果
    cv2.imshow('result', frame)
    # 等待按键触发
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
# 释放资源
cap.release()
cv2.destroyAllWindows()