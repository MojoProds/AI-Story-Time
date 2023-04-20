package com.mojo.productions.aistorytime.util

sealed class LoadResult<T>(val data: T? = null, val message: String? = null) {
  //class Loading<T>(data: T? = null) : LoadResult<T>(data)
  class Success<T>(data: T) : LoadResult<T>(data)
  class Error<T>(message: String, data: T? = null) : LoadResult<T>(data, message)
}
