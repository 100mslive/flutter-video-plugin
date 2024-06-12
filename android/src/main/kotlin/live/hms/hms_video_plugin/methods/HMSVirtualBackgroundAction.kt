package live.hms.hms_video_plugin

import android.graphics.BitmapFactory
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel.Result
import live.hms.hmssdk_flutter.HMSCommonAction
import live.hms.hmssdk_flutter.HMSErrorLogger
import live.hms.video.sdk.HMSSDK
import live.hms.video.virtualbackground.HMSVirtualBackground

class HMSVirtualBackgroundAction {

    companion object{

        fun virtualBackgroundActions(
            call: MethodCall,
            result: Result,
            hmssdk: HMSSDK?,
        ){
            when(call.method){
                "is_virtual_background_supported" -> isSupported(result,hmssdk)
                "enable_virtual_background" -> enable(call, result, hmssdk)
                "disable_virtual_background" -> disable(result, hmssdk)
                "enable_blur_background" -> enableBlur(call, result, hmssdk)
                "disable_blur_background" -> disableBlur(result, hmssdk)
                "change_virtual_background" -> changeVirtualBackground(call,result)
                else -> result.notImplemented()
            }
        }

        /**
         * [virtualBackgroundPlugin] stores the virtual background filter
         */
        private var virtualBackgroundPlugin: HMSVirtualBackground? = null
        private fun enable(call: MethodCall, result: Result, hmssdk: HMSSDK?){
            val imageByteArray: ByteArray? = call.argument<ByteArray?>("image")
            imageByteArray?.let { imageBitmap ->
                val vbImage = BitmapFactory.decodeByteArray(imageBitmap, 0, imageBitmap.size)
                hmssdk?.let {_hmssdk ->

                    /**
                     * If [virtualBackgroundPlugin] is null we set the virtualBackgroundPlugin
                     * and then call the [enableBackground] method
                     * else we directly call the [enableBackground] method
                     */
                    if(virtualBackgroundPlugin == null){
                        virtualBackgroundPlugin = HMSVirtualBackground(_hmssdk)
                        hmssdk.addPlugin(virtualBackgroundPlugin!!,HMSCommonAction.getActionListener(result))
                        virtualBackgroundPlugin?.enableBackground(vbImage)
                    }else{
                        virtualBackgroundPlugin?.enableBackground(vbImage)
                        result.success(null)
                    }
                }?:run{
                    HMSErrorLogger.logError("enableVirtualBackground","hmssdk is null","NULL ERROR")
                    result.success(null)
                }
            }?:run{
                HMSErrorLogger.returnArgumentsError("image can't be null")
                result.success(null)
            }
        }

        private fun changeVirtualBackground(call: MethodCall, result: Result){
            val imageByteArray: ByteArray? = call.argument<ByteArray?>("image")
            imageByteArray?.let { imageBitmap ->
                val vbImage = BitmapFactory.decodeByteArray(imageBitmap, 0, imageBitmap.size)
                virtualBackgroundPlugin?.enableBackground(vbImage)
                result.success(null)
            }?:run{
                HMSErrorLogger.returnArgumentsError("image can't be null")
                result.success(null)
            }
            result.success(null)
        }

        private fun disable(result: Result, hmssdk: HMSSDK?){
            hmssdk?.let {_hmssdk ->
                virtualBackgroundPlugin?.let { _virtualBackgroundPlugin ->
                    _hmssdk.removePlugin(_virtualBackgroundPlugin,HMSCommonAction.getActionListener(result))
                    virtualBackgroundPlugin = null
                }?:run{
                    HMSErrorLogger.logError("disableVirtualBackground","No virtual background plugin found","NULL ERROR")
                    result.success(null)
                }
            }?:run{
                HMSErrorLogger.logError("disableVirtualBackground","hmssdk is null","NULL ERROR")
                result.success(null)
            }
        }

        private fun enableBlur(call: MethodCall, result: Result, hmssdk: HMSSDK?){
            val blurRadius = call.argument<Int>("blur_radius")

            blurRadius?.let { _blurRadius ->
                hmssdk?.let { _hmssdk ->
                    /**
                     * If [virtualBackgroundPlugin] is null we set the virtualBackgroundPlugin
                     * and then call the [enableBlur] method
                     * else we directly call the [enableBlur] method
                     */
                    if(virtualBackgroundPlugin == null){
                        virtualBackgroundPlugin = HMSVirtualBackground(_hmssdk)
                        hmssdk.addPlugin(virtualBackgroundPlugin!!,HMSCommonAction.getActionListener(result))
                        virtualBackgroundPlugin?.enableBlur(_blurRadius)
                    }else{
                        virtualBackgroundPlugin?.enableBlur(_blurRadius)
                        result.success(null)
                    }
                }?:run{
                    HMSErrorLogger.logError("enableBlur","hmssdk is null","NULL ERROR")
                    result.success(null)
                }
            }?: run {
                HMSErrorLogger.returnArgumentsError("blurRadius can't be null")
                result.success(null)
            }
        }

        private fun disableBlur(result: Result, hmssdk: HMSSDK?){
            hmssdk?.let {_hmssdk ->
                virtualBackgroundPlugin?.let {
                    _hmssdk.removePlugin(it,HMSCommonAction.getActionListener(result))
                    virtualBackgroundPlugin = null
                }?: run {
                    HMSErrorLogger.logError("disableBlur","No blur plugin found","NULL ERROR")
                    result.success(null)
                }
            }?:run{
                HMSErrorLogger.logError("disableBlur","hmssdk is null","NULL ERROR")
                result.success(null)
            }
        }

        /**
         * [isSupported] method can be used to check whether virtual background is supported or not
         */
        private fun isSupported(result: Result, hmssdk: HMSSDK?){

            /**
             * If [virtualBackgroundPlugin] is null we set the plugin else we just return the result
             * of `isSupported` method
             */
            if(virtualBackgroundPlugin == null){
                hmssdk?.let { _hmssdk ->
                    virtualBackgroundPlugin = HMSVirtualBackground(_hmssdk)
                }?:run {
                    HMSErrorLogger.logError("disableBlur","hmssdk is null","NULL ERROR")
                    result.success(HMSResultExtension.toDictionary(success = false, data = false))
                    return
                }
            }
            virtualBackgroundPlugin?.let {_virtualBackgroundPlugin ->
                result.success(HMSResultExtension.toDictionary(true,_virtualBackgroundPlugin.isSupported()))
            }?:run{
                result.success(HMSResultExtension.toDictionary(success = true, data = false))
            }
        }
    }
}