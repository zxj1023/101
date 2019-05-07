/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package universalimageloader.core;

import java.util.concurrent.locks.ReentrantLock;

import universalimageloader.utils.MemoryCacheUtils;

/**
 * Information for load'n'display image task
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @see MemoryCacheUtils
 * @see DisplayImageOptions
 * @see universalimageloader.core.listener.ImageLoadingListener
 * @see universalimageloader.core.listener.ImageLoadingProgressListener
 * @since 1.3.1
 */
final class ImageLoadingInfo {

	final String uri;
	final String memoryCacheKey;
	final universalimageloader.core.imageaware.ImageAware imageAware;
	final universalimageloader.core.assist.ImageSize targetSize;
	final DisplayImageOptions options;
	final universalimageloader.core.listener.ImageLoadingListener listener;
	final universalimageloader.core.listener.ImageLoadingProgressListener progressListener;
	final ReentrantLock loadFromUriLock;

	public ImageLoadingInfo(String uri, universalimageloader.core.imageaware.ImageAware imageAware, universalimageloader.core.assist.ImageSize targetSize, String memoryCacheKey,
                            DisplayImageOptions options, universalimageloader.core.listener.ImageLoadingListener listener,
                            universalimageloader.core.listener.ImageLoadingProgressListener progressListener, ReentrantLock loadFromUriLock) {
		this.uri = uri;
		this.imageAware = imageAware;
		this.targetSize = targetSize;
		this.options = options;
		this.listener = listener;
		this.progressListener = progressListener;
		this.loadFromUriLock = loadFromUriLock;
		this.memoryCacheKey = memoryCacheKey;
	}
}
