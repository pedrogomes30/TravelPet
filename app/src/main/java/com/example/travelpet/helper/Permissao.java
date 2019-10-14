package com.example.travelpet.helper;

//import android.support.v4.app.Fragment;
// AINDA PRECISA SER TRABALHADO
// Esse sistema de permissão surgiu aparti da versão marshmallow que e API 23
public class Permissao {

    /*
    public static boolean validarPermissoes(String[] permissoes, Fragment fragment, int requestCode){

        // Verifica qual é a versão do android
        // Se for maior ou igual a 23 ele executa
        // SDK_INT recupera para a versao que está utilizando o APP
        if( Build.VERSION.SDK_INT >= 23 ){
            // Lista de Permissões
            List<String> listaPermissoes = new ArrayList<>();

             //Percorre as permissões passadas como parâmetro,
               //vericando uma a uma
               //se já tem a permissão liberada

            // Verifica se o usuário já tem as duas permissões passadas pelo parâmentro
            // String permissao = Recupera a primeira permissão
            // permissoes = Passa as permissões passadas por parâmetro
            for (String permissao : permissoes){
                // Validação
                // checkSelfPermission = Checa(recupera) se já tem a permissão concedida
                // As permissões uma vez que foi concedida elea fica salva e é gerenciada
                // pelo próprio Android utilizando o Package verificamos isso e compara
                // o "checkSelfPermission" com o "PackageManager.PERMISSION_GRANTED;"
                // == PackageManager.PERMISSION_GRANTED = Verifica se a permissão ja foi concedida
                Boolean temPermissao = ContextCompat.checkSelfPermission(fragment,permissao) == PackageManager.PERMISSION_GRANTED;
                // Caso não tenha permissão
                if(!temPermissao) listaPermissoes.add(permissao);
            }
            // Caso a lista estreja vazia, não é necessário solicitar permissão
            if( listaPermissoes.isEmpty()) return true;

            //  Convertendo uma Lista em Array para poder usar como parâmetro no requestPermissions
            // listaPermissoes.size() = Ver o tamanho da lista
            String[] novasPermissoes = new String[listaPermissoes.size()];
            // Convertendo a lista "listaPermissooes" para o array "novasPermissões"
            listaPermissoes.toArray( novasPermissoes );

            // Solicita Permissão
            // requestPermissions() = Exige 3 parâmetros, 1º activity, 2º Array de permissões, 3º requestcode
            ActivityCompat.requestPermissions(fragment,novasPermissoes, requestCode);
        }

        // POr padrão coloca "return true;" para parar de dar mensagem de erro
        return true;
    }

     */


}
