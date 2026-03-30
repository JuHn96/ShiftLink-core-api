package com.shiftlink.core_api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * ShiftLink Core API 서버 기동 진입점 (Main Entry Point)
 * 
 * @SpringBootApplication: 
 * 스프링 부트의 핵심 뼈대인 외부 자동 설정(Auto-Configuration)과
 * 모든 의존성 스캔(Component Scan)을 총괄하여 로드해주는 필수 환경 어노테이션
 */
@SpringBootApplication
class CoreApiApplication

/**
 * 애플리케이션 메모리 컴파일 및 실행 메인 함수
 * 내장 톰캣(Tomcat)을 구동하고 Spring Context를 로딩하여 실제 서버 구동을 시작한다.
 */
fun main(args: Array<String>) {
	runApplication<CoreApiApplication>(*args)
}
