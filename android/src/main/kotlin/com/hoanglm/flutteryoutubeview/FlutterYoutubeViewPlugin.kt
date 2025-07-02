package com.hoanglm.flutteryoutubeview

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.lifecycle.Lifecycle
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.PluginRegistry
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class FlutterYoutubeViewPlugin : FlutterPlugin, ActivityAware, Application.ActivityLifecycleCallbacks {
    private var activityBinding: ActivityPluginBinding? = null
    private val lifecycleChannel = MutableStateFlow(Lifecycle.Event.ON_CREATE)
    private var registrarActivityHashCode: Int? = null

    // post 1.12 android projects
    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        binding.platformViewRegistry.registerViewFactory(
                "plugins.hoanglm.com/youtube",
                YoutubeFactory(binding.binaryMessenger, lifecycleChannel)
        )
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        // do nothing
    }

    // This call will be followed by onReattachedToActivityForConfigChanges().
    override fun onDetachedFromActivity() {
        activityBinding?.activity?.application?.unregisterActivityLifecycleCallbacks(this)
        activityBinding = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
            onAttachedToActivity(binding)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
        registrarActivityHashCode = binding.activity.hashCode()
        binding.activity.application.registerActivityLifecycleCallbacks(this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }
    
    // lifecycle callbacks interface methods
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        GlobalScope.launch {
            lifecycleChannel.value = Lifecycle.Event.ON_CREATE
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        GlobalScope.launch {
            lifecycleChannel.value = Lifecycle.Event.ON_START
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        GlobalScope.launch {
            lifecycleChannel.value = Lifecycle.Event.ON_RESUME
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        GlobalScope.launch {
            lifecycleChannel.value = Lifecycle.Event.ON_PAUSE
        }
    }


    override fun onActivityStopped(activity: Activity) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        GlobalScope.launch {
            lifecycleChannel.value = Lifecycle.Event.ON_STOP
        }
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity.hashCode() != registrarActivityHashCode) {
            return
        }
        GlobalScope.launch {
            lifecycleChannel.value = Lifecycle.Event.ON_DESTROY
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
}
