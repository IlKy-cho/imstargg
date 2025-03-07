package com.imstargg.core.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BattleReaderTest {

    @Mock
    private BattleRepository battleRepository;

    @InjectMocks
    private BattleReader battleReader;

    @Test
    void 플레이어의_전적을_조회한다() {
        // given
        Player player = new PlayerFixture().build();
        int page = 1;
        List<PlayerBattle> playerBattles = List.of(new PlayerBattleFixture().build());
        given(battleRepository.find(player, page)).willReturn(new Slice<>(
                playerBattles, false
        ));

        // when
        var result = battleReader.getList(player, page);

        // then
        assertThat(result.content()).isEqualTo(playerBattles);
        assertThat(result.hasNext()).isFalse();
    }
}