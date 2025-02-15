export const isBrawlStarsTag = (tag: string): boolean => {
  const regex = /^#[A-Z0-9]+$/;
  return regex.test(tag);
}