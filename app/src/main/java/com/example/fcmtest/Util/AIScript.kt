package com.example.fcmtest.Util

class AIScript(script: String) {
    private val paragraphs = script.split("\n\n") // 전체 문단 분리
    private val situationAnalysis = paragraphs.getOrNull(0) ?: "내용 없음"

    // 응급 대응 파트는 2번째 문단부터
    private val emergencyParagraphs = paragraphs.drop(1).joinToString("\n\n")

    // "1. ", "2. ", ... 형태로 시작하는 응급 단계 문단 분리
    private val emergencySteps = Regex("""(?=\d+\.)""") // 숫자 + 점 + 공백 패턴으로 나누기
        .split(emergencyParagraphs)
        .map { it.trim() }
        .filter { it.isNotBlank() }

    // 페이지 구성: 첫 페이지는 "상황 분석", 나머지는 응급 대응 단계별
    private val pages = listOf("상황 분석" to situationAnalysis) +
            emergencySteps.mapIndexed { index, step ->
                "응급 단계 ${index + 1}" to step
            }

    fun getScript() : List<Pair<String, String>> {
        return pages
    }
}