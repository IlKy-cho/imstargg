export default interface Player {
  tag: string;
  name: string;
  nameColor: string;
  iconId: number;
  trophies: number;
  highestTrophies: number;
  clubTag: string | null;
  updatedAt: Date;
}