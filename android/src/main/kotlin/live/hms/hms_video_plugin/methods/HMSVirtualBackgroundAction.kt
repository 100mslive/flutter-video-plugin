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
                "pre_initialize_virtual_background" -> preInitialize(result,hmssdk)
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

        /**
         * [isPluginInPipeline] tracks whether the plugin has been successfully added to the SDK pipeline
         */
        private var isPluginInPipeline: Boolean = false
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
                        android.util.Log.d("HMSVirtualBG", "Plugin not pre-initialized, adding it now")
                        virtualBackgroundPlugin = HMSVirtualBackground(_hmssdk)
                        hmssdk.addPlugin(virtualBackgroundPlugin!!, object : live.hms.video.sdk.HMSActionResultListener {
                            override fun onSuccess() {
                                isPluginInPipeline = true
                                android.util.Log.d("HMSVirtualBG", "Plugin added to pipeline, enabling virtual background")
                                virtualBackgroundPlugin?.enableBackground(vbImage)
                                result.success(null)
                            }

                            override fun onError(error: live.hms.video.error.HMSException) {
                                HMSErrorLogger.logError("enableVirtualBackground", "Failed to add plugin: ${error.message}", "PLUGIN_ERROR")
                                isPluginInPipeline = false
                                result.success(live.hms.hmssdk_flutter.HMSExceptionExtension.toDictionary(error))
                            }
                        })
                    }else{
                        android.util.Log.d("HMSVirtualBG", "Plugin instance exists (inPipeline=$isPluginInPipeline), enabling virtual background")
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
                    _hmssdk.removePlugin(_virtualBackgroundPlugin,object : live.hms.video.sdk.HMSActionResultListener {
                        override fun onSuccess() {
                            isPluginInPipeline = false
                            virtualBackgroundPlugin = null
                            result.success(null)
                        }

                        override fun onError(error: live.hms.video.error.HMSException) {
                            isPluginInPipeline = false
                            virtualBackgroundPlugin = null
                            result.success(live.hms.hmssdk_flutter.HMSExceptionExtension.toDictionary(error))
                        }
                    })
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
                        android.util.Log.d("HMSVirtualBG", "Plugin not pre-initialized, adding it now")
                        virtualBackgroundPlugin = HMSVirtualBackground(_hmssdk)
                        hmssdk.addPlugin(virtualBackgroundPlugin!!, object : live.hms.video.sdk.HMSActionResultListener {
                            override fun onSuccess() {
                                // Plugin added successfully, now enable blur
                                isPluginInPipeline = true
                                android.util.Log.d("HMSVirtualBG", "Plugin added to pipeline, enabling blur with radius: $_blurRadius")
                                virtualBackgroundPlugin?.enableBlur(_blurRadius)
                                result.success(null)
                            }

                            override fun onError(error: live.hms.video.error.HMSException) {
                                HMSErrorLogger.logError("enableBlur", "Failed to add plugin: ${error.message}", "PLUGIN_ERROR")
                                isPluginInPipeline = false
                                result.success(live.hms.hmssdk_flutter.HMSExceptionExtension.toDictionary(error))
                            }
                        })
                    }else{
                        android.util.Log.d("HMSVirtualBG", "Plugin instance exists (inPipeline=$isPluginInPipeline), enabling blur with radius: $_blurRadius")
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
                virtualBackgroundPlugin?.let { _virtualBackgroundPlugin ->
                    _hmssdk.removePlugin(_virtualBackgroundPlugin, object : live.hms.video.sdk.HMSActionResultListener {
                        override fun onSuccess() {
                            isPluginInPipeline = false
                            virtualBackgroundPlugin = null
                            result.success(null)
                        }

                        override fun onError(error: live.hms.video.error.HMSException) {
                            isPluginInPipeline = false
                            virtualBackgroundPlugin = null
                            result.success(live.hms.hmssdk_flutter.HMSExceptionExtension.toDictionary(error))
                        }
                    })
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

        /**
         * [preInitialize] method pre-initializes the virtual background plugin
         * by creating it and adding it to the SDK pipeline.
         * This should be called after onJoin or onPreview callbacks.
         *
         * This ensures the plugin is in the video processing pipeline before
         * the user attempts to enable blur or virtual background.
         */
        private fun preInitialize(result: Result, hmssdk: HMSSDK?){
            hmssdk?.let { _hmssdk ->
                /**
                 * If [virtualBackgroundPlugin] is already initialized, just return success
                 */
                if(virtualBackgroundPlugin != null){
                    android.util.Log.d("HMSVirtualBG", "VB Plugin already initialized")
                    result.success(null)
                    return
                }

                /**
                 * Create the plugin and add it to the SDK pipeline
                 */
                virtualBackgroundPlugin = HMSVirtualBackground(_hmssdk)

                if(!virtualBackgroundPlugin!!.isSupported()){
                    android.util.Log.w("HMSVirtualBG", "VB not supported on this device")
                    virtualBackgroundPlugin = null
                    result.success(null)
                    return
                }

                android.util.Log.d("HMSVirtualBG", "Pre-initializing VB Plugin and adding to SDK pipeline")
                _hmssdk.addPlugin(virtualBackgroundPlugin!!, object : live.hms.video.sdk.HMSActionResultListener {
                    override fun onSuccess() {
                        isPluginInPipeline = true
                        android.util.Log.d("HMSVirtualBG", "✅ VB Plugin successfully added to SDK pipeline")
                        result.success(null)
                    }

                    override fun onError(error: live.hms.video.error.HMSException) {
                        android.util.Log.e("HMSVirtualBG", "❌ Failed to add VB plugin to SDK: ${error.message}")
                        HMSErrorLogger.logError("preInitialize", "Failed to add plugin: ${error.message}", "PLUGIN_ERROR")
                        isPluginInPipeline = false
                        virtualBackgroundPlugin = null
                        result.success(live.hms.hmssdk_flutter.HMSExceptionExtension.toDictionary(error))
                    }
                })
            }?:run {
                HMSErrorLogger.logError("preInitialize","hmssdk is null","NULL ERROR")
                result.success(null)
            }
        }

        /**
         * [clearPluginState] clears the plugin state and pipeline flag.
         * This should be called when the SDK is being destroyed or recreated
         * to prevent stale references.
         */
        fun clearPluginState() {
            android.util.Log.d("HMSVirtualBG", "Clearing plugin state")
            virtualBackgroundPlugin = null
            isPluginInPipeline = false
        }
    }
}