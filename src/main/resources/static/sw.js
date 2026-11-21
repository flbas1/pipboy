const CACHE_NAME = 'party-engine-v1';
const MENU_URL = '/menu';

self.addEventListener('install', event => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => cache.addAll([
                '/',
                '/index.html',
                '/pipboy.css',
                '/pipboy.js',
                '/manifest.json',
                '/map.png'
            ]))
            .then(() => self.skipWaiting())
    );
});
self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys().then(keys =>
            Promise.all(
                keys
                    .filter(key => key !== CACHE_NAME)
                    .map(key => caches.delete(key))
            )
        ).then(() => self.clients.claim())
    );
});

// self.addEventListener('fetch', event => {
//     const url = new URL(event.request.url);

//     // Menu: cache after the first successful download
//     if (url.pathname === MENU_URL) {
//         event.respondWith(
//             caches.match(event.request)
//                 .then(cached => {
//                     if (cached) {
//                         return cached;
//                     }

//                     return fetch(event.request)
//                         .then(response => {
//                             if (response.ok) {
//                                 const copy = response.clone();

//                                 caches.open(CACHE_NAME)
//                                     .then(cache => cache.put(event.request, copy));
//                             }

//                             return response;
//                         });
//                 })
//         );

//         return;
//     }

self.addEventListener('fetch', event => {
    event.respondWith(
        caches.match(event.request)
            .then(cached => cached || fetch(event.request))
    );
});

    // Everything else: normal network request
    event.respondWith(
        fetch(event.request)
            .catch(() => caches.match(event.request))
    );
});

// navigator.serviceWorker.controller?.postMessage({
//     type: 'CLEAR_CACHE'
// });

self.addEventListener('message', event => {
    if (event.data?.type === 'CLEAR_CACHE') {
        caches.delete(CACHE_NAME);
    }
});