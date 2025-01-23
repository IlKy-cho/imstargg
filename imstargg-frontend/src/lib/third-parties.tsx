import {GoogleAnalytics as NextGoogleAnalytics} from '@next/third-parties/google'


export function GoogleAnalytics() {
  return (
    process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS && (
      <NextGoogleAnalytics gaId={process.env.NEXT_PUBLIC_GOOGLE_ANALYTICS}/>
    )
  );
}
