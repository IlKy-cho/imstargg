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

export default function PlayerPage({params}: Readonly<Props>) {

  return (
    <div className="space-y-4">
      <Player tag={params.tag}/>
      <PlayerBattleList tag={params.tag}/>
    </div>
  );
}
