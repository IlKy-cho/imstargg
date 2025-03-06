import {GoogleAnalytics as NextGoogleAnalytics} from '@next/third-parties/google'
import Script from "next/script";


export function GoogleAnalytics() {
  return (
    process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS && (
      <NextGoogleAnalytics gaId={process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS}/>
    )
  );
}

export function GoogleAdSense() {
  return (
    <Script
      async
      src="https://pagead2.googlesyndication.com/pagead/js/adsbygoogle.js?client=ca-pub-1486181177977193"
      crossOrigin="anonymous"
      strategy="lazyOnload"
    />
  );
}
