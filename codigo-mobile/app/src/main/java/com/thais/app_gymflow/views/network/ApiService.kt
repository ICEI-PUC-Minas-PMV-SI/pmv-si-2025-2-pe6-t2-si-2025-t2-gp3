package com.thais.app_gymflow.views.network

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("auth/login")
    suspend fun login2(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/login")
    suspend fun login3(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("api/usuarios/login")
    suspend fun login4(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("authenticate")
    suspend fun login5(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/treinos")
    suspend fun getTreinosPorUsuario(@Query("usuarioId") usuarioId: Long): Response<TreinoListResponse>

    @POST("api/treinos")
    suspend fun criarTreino(@Body treino: TreinoRequest): Response<TreinoResponse>

    @DELETE("api/treinos/{id}")
    suspend fun deletarTreino(@Path("id") id: Long): Response<Void>

    @GET("api/fichas/treino/{treinoId}")
    suspend fun getFichasPorTreino(@Path("treinoId") treinoId: Long): Response<List<FichaCadastroResponse>>

    @POST("api/fichas")
    suspend fun criarFicha(@Body ficha: FichaCadastroRequest): Response<FichaCadastroResponse>

    @DELETE("api/fichas/{id}")
    suspend fun deletarFicha(@Path("id") id: Long): Response<Void>

    @GET("api/exercicios/ficha/{fichaId}")
    suspend fun getExerciciosPorFicha(@Path("fichaId") fichaId: Long): Response<List<ExercicioCadastroResponse>>

    @POST("api/exercicios")
    suspend fun criarExercicio(@Body exercicio: ExercicioCadastroRequest): Response<ExercicioCadastroResponse>

    @DELETE("api/exercicios/{id}")
    suspend fun deletarExercicio(@Path("id") id: Long): Response<Void>

    @GET("api/usuarios")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @POST("api/usuarios")
    suspend fun criarUsuario(@Body usuario: Usuario): Response<Usuario>

    @GET("api/usuarios/{usuarioId}/metas")
    suspend fun getMetasPorUsuario(@Path("usuarioId") usuarioId: Long): Response<MetaListResponse>

    @POST("api/usuarios/{usuarioId}/metas")
    suspend fun criarMeta(
        @Path("usuarioId") usuarioId: Long,
        @Body meta: MetaRequest
    ): Response<MetaResponse>

    @POST("api/metas")
    suspend fun criarMeta2(@Body meta: MetaRequest): Response<MetaResponse>

    @POST("metas")
    suspend fun criarMeta3(@Body meta: MetaRequest): Response<MetaResponse>

    @POST("api/usuarios/metas")
    suspend fun criarMeta4(@Body meta: MetaRequest): Response<MetaResponse>

    @PUT("api/metas/{id}")
    suspend fun atualizarMeta(
        @Path("id") id: Long,
        @Body meta: MetaRequest
    ): Response<MetaResponse>

    @DELETE("api/metas/{id}")
    suspend fun deletarMeta(@Path("id") id: Long): Response<Void>

    @GET("api/usuarios")
    suspend fun getUsuarioPorId(@Query("idUsuario") usuarioId: Long): Response<UsuarioPerfilResponse>

    @PUT("api/usuarios")
    suspend fun atualizarUsuario(
        @Query("idUsuario") usuarioId: Long,
        @Body usuario: UsuarioUpdateRequest
    ): Response<UsuarioPerfilResponse>
}