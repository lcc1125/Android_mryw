package com.example.myapplication.ui.practice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

/**
 * 语音识别辅助类
 * 使用Android内置的SpeechRecognizer进行语音识别
 */
public class SpeechRecognitionHelper {

    private static final String TAG = "SpeechRecognitionHelper";

    private final Context context;
    private SpeechRecognizer speechRecognizer;
    private RecognitionCallback callback;

    /**
     * 是否正在识别
     */
    private boolean isListening = false;

    public SpeechRecognitionHelper(Context context) {
        this.context = context;
    }

    /**
     * 识别结果回调接口
     */
    public interface RecognitionCallback {
        /**
         * 识别成功
         * @param text 识别的文本
         */
        void onResult(String text);

        /**
         * 识别进行中（部分结果）
         * @param text 部分识别的文本
         */
        void onPartialResult(String text);

        /**
         * 准备开始识别
         */
        void onReadyForSpeech();

        /**
         * 开始说话
         */
        void onBeginningOfSpeech();

        /**
         * 说话结束
         */
        void onEndOfSpeech();

        /**
         * 音量变化
         * @param rmsdB 音量值
         */
        void onVolumeChanged(float rmsdB);

        /**
         * 识别错误
         * @param error 错误信息
         * @param errorCode 错误码
         */
        void onError(String error, int errorCode);

        /**
         * 识别停止
         */
        void onStop();
    }

    /**
     * 设置回调
     */
    public void setCallback(RecognitionCallback callback) {
        this.callback = callback;
    }

    /**
     * 创建语音识别器
     */
    public void createSpeechRecognizer() {
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Log.d(TAG, "onReadyForSpeech");
                    isListening = true;
                    if (callback != null) {
                        callback.onReadyForSpeech();
                    }
                }

                @Override
                public void onBeginningOfSpeech() {
                    Log.d(TAG, "onBeginningOfSpeech");
                    if (callback != null) {
                        callback.onBeginningOfSpeech();
                    }
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                    if (callback != null) {
                        callback.onVolumeChanged(rmsdB);
                    }
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                    // 不需要处理
                }

                @Override
                public void onEndOfSpeech() {
                    Log.d(TAG, "onEndOfSpeech");
                    if (callback != null) {
                        callback.onEndOfSpeech();
                    }
                }

                @Override
                public void onError(int error) {
                    Log.e(TAG, "onError: " + error);
                    isListening = false;

                    String errorMessage;
                    switch (error) {
                        case SpeechRecognizer.ERROR_AUDIO:
                            errorMessage = "音频录制错误";
                            break;
                        case SpeechRecognizer.ERROR_CLIENT:
                            errorMessage = "客户端错误";
                            break;
                        case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                            errorMessage = "权限不足";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK:
                            errorMessage = "网络错误";
                            break;
                        case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                            errorMessage = "网络超时";
                            break;
                        case SpeechRecognizer.ERROR_NO_MATCH:
                            errorMessage = "未识别到语音";
                            break;
                        case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                            errorMessage = "识别器忙";
                            break;
                        case SpeechRecognizer.ERROR_SERVER:
                            errorMessage = "服务器错误";
                            break;
                        case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                            errorMessage = "没有检测到语音输入";
                            break;
                        default:
                            errorMessage = "未知错误: " + error;
                            break;
                    }

                    if (callback != null) {
                        callback.onError(errorMessage, error);
                    }
                }

                @Override
                public void onResults(Bundle results) {
                    Log.d(TAG, "onResults");
                    isListening = false;

                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String text = matches.get(0); // 获取最匹配的结果
                        if (callback != null) {
                            callback.onResult(text);
                        }
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                    Log.d(TAG, "onPartialResults");

                    ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String text = matches.get(0);
                        if (callback != null) {
                            callback.onPartialResult(text);
                        }
                    }
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                    // 不需要处理
                }
            });
        }
    }

    /**
     * 开始语音识别
     * @param language 识别语言，如 "zh-CN" 中文简体
     */
    public void startListening(String language) {
        if (speechRecognizer == null) {
            createSpeechRecognizer();
        }

        if (!isListening) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language);
            intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true); // 启用部分结果
            intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1); // 只返回最佳结果

            speechRecognizer.startListening(intent);
            Log.d(TAG, "startListening");
        }
    }

    /**
     * 开始语音识别（使用默认语言中文）
     */
    public void startListening() {
        startListening("zh-CN");
    }

    /**
     * 停止语音识别
     */
    public void stopListening() {
        if (speechRecognizer != null && isListening) {
            speechRecognizer.stopListening();
            Log.d(TAG, "stopListening");
        }
    }

    /**
     * 取消语音识别
     */
    public void cancelListening() {
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            isListening = false;
            Log.d(TAG, "cancelListening");

            if (callback != null) {
                callback.onStop();
            }
        }
    }

    /**
     * 释放资源
     */
    public void destroy() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer = null;
            isListening = false;
            Log.d(TAG, "destroy");
        }
    }

    /**
     * 检查是否正在识别
     */
    public boolean isListening() {
        return isListening;
    }
}
