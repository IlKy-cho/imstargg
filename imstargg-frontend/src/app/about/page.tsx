import {meta} from '@/config/site';

export default async function AboutPage() {
  return (
    <div>
      <div className="bg-cover bg-center bg-no-repeat bg-loading-screen p-4 py-20">
        <div className="p-2 rounded-lg shadow-lg border bg-zinc-100 text-xl font-bold text-center">
          <p>한글로된 브롤스타즈 통계 사이트가 없어서 한번 만들어봅니다.</p>
          <p>겸사겸사 제 브롤스타즈 티어도 오르면 좋구요..ㅎ</p>
        </div>
      </div>
      <div>
        <div className="sm:p-4 p-2">
          <h2 className="text-xl font-bold">안녕하세요.</h2>
          <p>저는 브롤스타즈 통계 사이트를 만들고 있는 개발자입니다.</p>
          <p>브롤스타즈 게임을 즐겨 하는데 하다보니 티어 욕심이 생기더라고요.</p>
          <p>이전에 리그오브레전드 하던 시절에 통계 사이트 많이 이용했었어서 브롤스타즈도 찾아봤는데 한글로된 통계 사이트가 없어서 만들어봅니다.</p>
        </div>
        <div className="sm:p-4 p-2">
          <h2 className="text-xl font-bold">열심히 만들어보겠습니다.</h2>
          <p>아직 만든지 얼마 안되서 버그도 있을 수 있고 기능도 많이 부족합니다.(24.11.19 부터 시작함)</p>
          <p>제가 열심히 만들어서 이것저것 추가해볼테니 재밌게 사용해주시면 감사드리곘습니다.</p>
        </div>
        <div className="sm:p-4 p-2">
          <h2 className="text-xl font-bold">문의, 소통은 감사합니다!</h2>
          <p>이메일 : {meta.email}</p>
          <p>혹시 생각하시는 기능이나 버그 등 문의주시고 싶으시면 편하게 연락주시면 감사드리겠습니다.</p>
          <p>이외의 소통 가능하고 아무말이나 주셔도 감사드리겠습니다. 😁</p>
          <p>좋은 하루되세요~</p>
        </div>
      </div>

    </div>
  );
}
