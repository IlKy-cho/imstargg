export default interface Slice<T> {
  content: T[];
  hasNext: boolean;
}