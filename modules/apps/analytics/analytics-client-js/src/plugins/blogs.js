import debounce from 'metal-debounce';
import {DEBOUNCE} from '../utils/constants';
import {getClosestAssetElement, getNumberOfWords} from '../utils/assets';
import {onReady} from '../utils/events.js';
import {ScrollTracker} from '../utils/scroll';

const applicationId = 'Blog';

/**
 * Returns analytics payload with Blog information.
 * @param {object} blog The blog DOM element
 * @return {object} The payload with blog information
 */
function getBlogPayload(blog) {
	const {dataset} = blog;

	let payload = {
		entryId: dataset.analyticsAssetId
	};

	if (dataset.analyticsAssetTitle) {
		payload = {
			...payload,
			title: dataset.analyticsAssetTitle
		};
	}

	return payload;
}

/**
 * Wether a Blog is trackable or not.
 * @param {object} element The Blog DOM element
 * @return {boolean} True if the element is trackable.
 */
function isTrackableBlog(element) {
	return element && 'analyticsAssetId' in element.dataset;
}

/**
 * Sends information about Blogs scroll actions.
 * @param {object} The Analytics client instance
 */
function trackBlogsScroll(analytics, blogElements) {
	const scrollSessionId = new Date().toISOString();
	const scrollTracker = new ScrollTracker();

	const onScroll = debounce(
		() => {
			blogElements.forEach(
				element => {
					scrollTracker.onDepthReached(
						depth => {
							analytics.send(
								'blogDepthReached',
								applicationId,
								{
									...getBlogPayload(element),
									depth,
									sessionId: scrollSessionId
								}
							);
						},
						element
					);
				}
			);
		},
		DEBOUNCE
	);

	document.addEventListener('scroll', onScroll);

	return () => {
		document.removeEventListener('scroll', onScroll);
	};
}

/**
 * Sends information when user scrolls on a Blog.
 * @param {object} The Analytics client instance
 */
function trackBlogViewed(analytics) {
	const blogElements = [];
	const stopTrackingOnReady = onReady(
		() => {
			Array.prototype.slice.call(
				document.querySelectorAll('[data-analytics-asset-type="blog"]')
			).filter(
				element => isTrackableBlog(element)
			).forEach(
				element => {
					const numberOfWords = getNumberOfWords(element);

					let payload = getBlogPayload(element);

					payload = {
						numberOfWords,
						...payload
					};

					blogElements.push(element);

					analytics.send('blogViewed', applicationId, payload);
				}
			);
		}
	);
	const stopTrackingBlogsScroll = trackBlogsScroll(analytics, blogElements);
	return () => {
		stopTrackingBlogsScroll();
		stopTrackingOnReady();
	};
}

/**
 * Sends information when user clicks on a Blog.
 * @param {object} The Analytics client instance
 */
function trackBlogClicked(analytics) {
	const onClick = ({target}) => {
		const blogElement = getClosestAssetElement(target, 'blog');

		if (!isTrackableBlog(blogElement)) {
			return;
		}

		const tagName = target.tagName.toLowerCase();

		const payload = {
			...getBlogPayload(blogElement),
			tagName
		};

		if (tagName === 'a') {
			payload.href = target.href;
			payload.text = target.innerText;
		}
		else if (tagName === 'img') {
			payload.src = target.src;
		}

		analytics.send('blogClicked', applicationId, payload);
	};

	document.addEventListener('click', onClick);

	return () => document.removeEventListener('click', onClick);
}

/**
 * Plugin function that registers listeners for Blog events
 * @param {object} analytics The Analytics client
 */
function blogs(analytics) {
	const stopTrackingBlogClicked = trackBlogClicked(analytics);
	const stopTrackingBlogViewed = trackBlogViewed(analytics);

	return () => {
		stopTrackingBlogClicked();
		stopTrackingBlogViewed();
	};
}

export {blogs};
export default blogs;