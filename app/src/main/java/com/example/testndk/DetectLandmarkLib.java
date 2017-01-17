package com.example.testndk;

public class DetectLandmarkLib {
	
	//初始化
	public static int InitFaceLandmark() {
		int mResult = -1;
		mResult = NonfreeJNILib.InitDet();
		return mResult;
	}
	
	//所有步骤检测完后释放全部指针
	public static void ReleaseFaceLandmark() {
		NonfreeJNILib.ReleaseDet();
	}
	
	//单步检测完成后释放部分内存
	public static void ReleaseTmpLandmark() {
		NonfreeJNILib.ReleaseTmp();
	}
	
	//人脸检测返回landmark点
	public static int ExecuteFaceLandmark(byte[] img) {
		int mResult = -1;
		mResult = NonfreeJNILib.DetLocate(img);
		return mResult;
	}
	
	//初始化结构
//	public static FaceInfo GetFaceInfo(int[] faces, int[] landmarks)
//	{
//		FaceInfo faceInfo = new FaceInfo();
//		if(faces == null)
//		{
//			return faceInfo;
//		}
//		if(landmarks ==null)
//		{
//			return faceInfo;
//		}
//		faceInfo.left_above_x=faces[0];
//		faceInfo.left_above_y=faces[1];
//		faceInfo.nWidth=faces[2];
//		faceInfo.nHeight=faces[3];
//		//鼻子的四个点
//		faceInfo.nose_x = new int[4];
//		faceInfo.nose_x[0] = landmarks[54];
//		faceInfo.nose_x[1] = landmarks[56];
//		faceInfo.nose_x[2] = landmarks[58];
//		faceInfo.nose_x[3] = landmarks[60];
//		faceInfo.nose_y = new int[4];
//		faceInfo.nose_y[0] = landmarks[55];
//		faceInfo.nose_y[1] = landmarks[57];
//		faceInfo.nose_y[2] = landmarks[59];
//		faceInfo.nose_y[3] = landmarks[61];
//		//左眼和右眼的分别四个点的纵坐标
//		faceInfo.leye_y = new int[4];
//		faceInfo.leye_y[0] = landmarks[87];
//		faceInfo.leye_y[1] = landmarks[89];
//		faceInfo.leye_y[2] = landmarks[93];
//		faceInfo.leye_y[3] = landmarks[95];
//		faceInfo.reye_y = new int[4];
//		faceInfo.reye_y[0] = landmarks[75];
//		faceInfo.reye_y[1] = landmarks[77];
//		faceInfo.reye_y[2] = landmarks[81];
//		faceInfo.reye_y[3] = landmarks[83];
//		//上下嘴唇的各三个点的纵坐标
//		faceInfo.mouth_above_y = new int[3];
//		faceInfo.mouth_above_y[0] = landmarks[123];
//		faceInfo.mouth_above_y[1] = landmarks[125];
//		faceInfo.mouth_above_y[2] = landmarks[127];
//		faceInfo.mouth_below_y = new int[3];
//		faceInfo.mouth_below_y[0] = landmarks[131];
//		faceInfo.mouth_below_y[1] = landmarks[133];
//		faceInfo.mouth_below_y[2] = landmarks[135];
//		//用于归一化的点的坐标
//		faceInfo.point_27_x = landmarks[54];
//		faceInfo.point_27_y = landmarks[55];
//		faceInfo.point_33_x = landmarks[66];
//		faceInfo.point_33_y = landmarks[67];
//		
//		return faceInfo;
//	}
	
	/*
	 * //判断人脸类型
	 * 参数：img   byte[]图片流，直接调用android摄像头时需要先使用PicProcessUtils.decodeYUV420SP 函数转换为RGB格式
	 *      width  脸的宽度
	 *      height 脸的高度
	 *      facecenter_x 脸中心点的横坐标
	 *      facecenter_y 脸中心点的纵坐标
	 *      left_x 左眼中心横坐标
	 *      left_y 左眼中心纵坐标
	 *      right_x 右眼中心横坐标
	 *      right_y 右眼中心纵坐标
	 *      eResult 检测结果
	 * */
	public static int ExecuteFaceJudgement(byte[] img, eFaceType[] eResult)
	{
		int mResult = 0;
		if (img == null){
			return -1;
		}
		if(eResult == null){
			return -1;
		}
		int[] judgeResult = new int[1];
		mResult = NonfreeJNILib.DetFacePos(img, judgeResult);
		
		switch(judgeResult[0])
		{
		case 0:
			eResult[0] = eFaceType.Type_Front;
			break;
		case 1:
			eResult[0] = eFaceType.Type_Side;
			break;
		/*case 2:
			eResult[0] = eFaceType.Type_Right;
			break;*/
		default:
			eResult[0] = eFaceType.Type_None;
			break;
			
		}
		return mResult;
	}
	
	/*
	 * //判断人眼状态
	 * 参数：img   byte[]图片流，直接调用android摄像头时需要先使用PicProcessUtils.decodeYUV420SP 函数转换为RGB格式
	 *      width  脸的宽度
	 *      height 脸的高度
	 *      lux 左上眼皮横坐标
	 *      luy 左上眼皮纵坐标
	 *      ldx 左下眼皮横坐标
	 *      ldy 左下眼皮纵坐标
	 *      rux 右上眼皮横坐标
	 *      ruy 右上眼皮纵坐标
	 *      rdx 右下眼皮横坐标
	 *      rdy 右下眼皮纵坐标
	 *      eResult 检测结果
	 * */
	public static int ExecuteEyeJudgement(byte[] img, eEyeStatus[] eResult)
	{
		int mResult = 0;
		if (img == null){
			return -1;
		}
		if(eResult == null){
			return -1;
		}
		int[] judgeResult = new int[1];
		mResult = NonfreeJNILib.DetEyeStatus(img,judgeResult);
		switch(judgeResult[0])
		{
		case 1:
			eResult[0] = eEyeStatus.Status_Close;
			break;
		case 0:
			eResult[0] = eEyeStatus.Status_Open;
			break;
		default:
			eResult[0] = eEyeStatus.Status_None;
			break;			
		}
		return mResult;
	}
	
	public static int ExecuteMouthJudgement(byte[] img, eMouthStatus[] eResult){
		
		int mResult = 0;
		if (img == null){
			return -1;
		}
		if(eResult == null){
			return -1;
		}
		int[] judgeResult = new int[1];
		mResult = NonfreeJNILib.DetMouthStatus(img, judgeResult);
		switch(judgeResult[0])
		{
		case 0:
			eResult[0] = eMouthStatus.Status_Shut;
			break;
		case 1:
			eResult[0] = eMouthStatus.Status_Open;
			break;
		default:
			eResult[0] = eMouthStatus.Status_None;
			break;			
		}
		return mResult;
	}
	
	
	/**
	 * 人脸类型
	 * Type_Front  //正脸
	 * Type_Left   //左侧脸
	 * Type_Right  //右侧脸
	 * Type_Up     //脸略微仰起
	 * Type_Down   //脸略微低头
	 * Type_None   //不确定
	 */
	public enum eFaceType {
	    Type_Front,
		Type_Side,
	    Type_Left, 
	    Type_Right,
	    Type_None
	}
	
	/**
	 * 人眼状态类型
	 * Status_Open    0  //睁眼
	 * Status_Close   1  //闭眼
	 * Status_None    -1 //不确定
	 */
	public enum eEyeStatus{
		Status_Open,
		Status_Close,
		Status_None
	}
	
	public enum eMouthStatus{
		Status_Open,
		Status_Shut,
		Status_None
	}
	
}

