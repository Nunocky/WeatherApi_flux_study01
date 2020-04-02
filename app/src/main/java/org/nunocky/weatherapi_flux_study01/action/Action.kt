package org.nunocky.weatherapi_flux_study01.action

class Action internal constructor(val type: String, private val data: HashMap<String, Any>?) {

    fun getData(): HashMap<String, Any>? {
        return data
    }

    class Builder {
        private var type: String? = null
        private var data: HashMap<String, Any>? = null
        fun with(type: String?): Builder {
            requireNotNull(type) { "Type may not be null." }
            this.type = type
            data = HashMap()
            return this
        }

        fun bundle(key: String?, value: Any?): Builder {
            requireNotNull(key) { "Key may not be null." }
            requireNotNull(value) { "Value may not be null." }
            data!![key] = value
            return this
        }

        fun build(): Action {
            require(!(type == null || type!!.isEmpty())) { "At least one key is required." }
            return Action(type!!, data)
        }
    }

    companion object {
        fun type(type: String?): Builder {
            return Builder().with(type)
        }
    }

}