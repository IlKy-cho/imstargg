import {getPlayer} from '@/app/_lib/getPlayer'
import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import {Player} from './_components/Player';
import PlayerBattleList from './_components/PlayerBattleList';

dayjs.locale('ko');
dayjs.extend(relativeTime);

type Props = {
  params: {
    tag: string
  }
};

export default async function PlayerPage({params}: Readonly<Props>) {
  const player = await getPlayer(params.tag);

  return (
    <div className="space-y-4">
      <Player player={player}/>
      <PlayerBattleList player={player}/>
    </div>
  );
}
