package com.anyidea.lunch.service;

import com.anyidea.lunch.dto.MenuDataDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MenuService {

    public MenuDataDto getMenuData() {
        List<String> roots = List.of("밥", "면", "빵/과자", "국/찌개", "고기", "한식", "중식", "양식");

        Map<String, List<String>> subs = Map.of(
                "밥", List.of("볶음밥/오므라이스", "카레", "덮밥", "비빔밥", "도시락", "백반", "죽", "주먹밥"),
                "면", List.of("국수", "라면", "스파게티", "파스타", "우동", "냉모밀", "짬뽕", "칼국수"),
                "빵/과자", List.of("피자", "샌드위치", "베이글", "버거", "토스트", "도넛", "크루아상", "파이"),
                "국/찌개", List.of("순두부찌개", "부대찌개", "김치찌개", "된장찌개", "국밥", "육개장", "해장국", "감자탕"),
                "고기", List.of("돈까스", "불고기", "제육볶음", "스테이크", "갈비찜", "삼계탕", "치킨", "양꼬치"),
                "한식", List.of("김치찌개", "된장찌개", "불고기", "비빔밥", "잡채", "삼계탕", "갈비찜", "제육볶음"),
                "중식", List.of("짜장면", "짬뽕", "탕수육", "마파두부", "양장피", "볶음밥", "깐풍기", "울면"),
                "양식", List.of("스파게티", "리조또", "피자", "스테이크", "파스타", "샐러드", "라자냐", "수프")
        );

        return new MenuDataDto(roots, subs);
    }
}
