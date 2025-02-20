export const isBrawlStarsTag = (tag: string): boolean => {
  const regex = /^#[A-Z0-9]{8,16}$/;
  return regex.test(tag);
}