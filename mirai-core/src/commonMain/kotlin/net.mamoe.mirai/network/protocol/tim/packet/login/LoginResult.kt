package net.mamoe.mirai.network.protocol.tim.packet.login

import net.mamoe.mirai.network.protocol.tim.packet.login.LoginResult.Companion.SUCCESS
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmStatic

/**
 * 登录结果. 除 [SUCCESS] 外均为失败.
 * @see LoginResult.requireSuccess 要求成功
 */
inline class LoginResult(val id: Byte) {
    companion object {
        /**
         * 登录成功
         */
        @JvmStatic
        val SUCCESS = LoginResult(0)

        /**
         * 密码错误
         */
        @JvmStatic
        val WRONG_PASSWORD = LoginResult(1)

        /**
         * 被冻结
         */
        @JvmStatic
        val BLOCKED = LoginResult(2)

        /**
         * QQ 号码输入有误
         */
        @JvmStatic
        val UNKNOWN_QQ_NUMBER = LoginResult(3)

        /**
         * 账号开启了设备锁. 暂不支持设备锁登录
         */
        @JvmStatic
        val DEVICE_LOCK = LoginResult(4)

        /**
         * 账号被回收
         */
        @JvmStatic
        val TAKEN_BACK = LoginResult(5)

        /**
         * 未知. 更换服务器或等几分钟再登录可能解决.
         */
        @JvmStatic
        val UNKNOWN = LoginResult(6)

        /**
         * 包数据错误
         */
        @JvmStatic
        val INTERNAL_ERROR = LoginResult(7)

        /**
         * 超时
         */
        @JvmStatic
        val TIMEOUT = LoginResult(8)
    }
}

/**
 * 如果 [this] 不为 [LoginResult.SUCCESS] 就抛出消息为 [lazyMessage] 的 [IllegalStateException]
 */
@UseExperimental(ExperimentalContracts::class)
inline fun LoginResult.requireSuccess(lazyMessage: (LoginResult) -> String) {
    contract {
        callsInPlace(lazyMessage, InvocationKind.AT_MOST_ONCE)
    }
    if (this != SUCCESS) error(lazyMessage(this))
}


/**
 * 检查 [this] 为 [LoginResult.SUCCESS].
 * 失败则 [error]
 */
fun LoginResult.requireSuccess() {
    if (requireSuccessOrNull() === null)
        error("Login failed: $this")
}

/**
 * 检查 [this] 为 [LoginResult.SUCCESS].
 * 失败则返回 `null`
 *
 * @return 成功时 [Unit], 失败时 `null`
 */
fun LoginResult.requireSuccessOrNull(): Unit? {
    return if (this == SUCCESS) Unit else null
}


/**
 * 检查 [this] 为 [LoginResult.SUCCESS].
 * 失败则返回 `null`
 *
 * @return 成功时 [Unit], 失败时 `null`
 */
@UseExperimental(ExperimentalContracts::class)
inline fun <R> LoginResult.ifFail(block: (LoginResult) -> R): R? {
    contract {
        callsInPlace(block, InvocationKind.AT_MOST_ONCE)
    }
    return if (this != SUCCESS) block(this) else null
}
