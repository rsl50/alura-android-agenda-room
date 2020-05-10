package br.com.alura.agenda.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.List;

import br.com.alura.agenda.R;
import br.com.alura.agenda.database.AgendaDatabase;
import br.com.alura.agenda.database.dao.AlunoDAO;
import br.com.alura.agenda.database.dao.TelefoneDAO;
import br.com.alura.agenda.model.Aluno;
import br.com.alura.agenda.model.Telefone;
import br.com.alura.agenda.model.TipoTelefone;

import static br.com.alura.agenda.ui.activity.ConstantesActivities.CHAVE_ALUNO;

public class FormularioAlunoActivity extends AppCompatActivity {

    private static final String TITULO_APPBAR_NOVO_ALUNO = "Novo aluno";
    private static final String TITULO_APPBAR_EDITA_ALUNO = "Edita aluno";
    private EditText campoNome;
    private EditText campoTelefoneFixo;
    private EditText campoTelefoneCelular;
    private EditText campoEmail;
    private AlunoDAO alunoDAO;
    private TelefoneDAO telefoneDAO;
    private Aluno aluno;
    private List<Telefone> telefonesDoAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_aluno);

        AgendaDatabase database = AgendaDatabase.getInstance(this);
        alunoDAO = database.getRoomAlunoDAO();
        telefoneDAO = database.getTelefoneDAO();
        inicializacaoDosCampos();
        carregaAluno();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.activity_formulario_aluno_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.activity_formulario_aluno_menu_salvar){
            finalizaFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    private void carregaAluno() {
        Intent dados = getIntent();
        if (dados.hasExtra(CHAVE_ALUNO)) {
            setTitle(TITULO_APPBAR_EDITA_ALUNO);
            aluno = (Aluno) dados.getSerializableExtra(CHAVE_ALUNO);
            preencheCampos();
        } else {
            setTitle(TITULO_APPBAR_NOVO_ALUNO);
            aluno = new Aluno();
        }
    }

    private void preencheCampos() {
        campoNome.setText(aluno.getNome());
        campoEmail.setText(aluno.getEmail());

        telefonesDoAluno = telefoneDAO.buscaTodosTelefonesDoAluno(aluno.getId());
        for (Telefone telefone: telefonesDoAluno) {
            if (telefone.getTipo() == TipoTelefone.FIXO){
                campoTelefoneFixo.setText(telefone.getNumero());
            } else {
                campoTelefoneCelular.setText(telefone.getNumero());
            }
        }

    }

    private void finalizaFormulario() {
        preencheAluno();
        if (aluno.temIdValido()) {
            alunoDAO.edita(aluno);

            for (Telefone telefone : telefonesDoAluno) {
                if (telefone.getTipo() == TipoTelefone.FIXO){
                    String numeroFixo = campoTelefoneFixo.getText().toString();
                    telefone.setNumero(numeroFixo);
                } else {
                    String numeroCelular = campoTelefoneCelular.getText().toString();
                    telefone.setNumero(numeroCelular);
                }
                telefoneDAO.atualiza(telefonesDoAluno);
            }
        } else {
            int alunoId = alunoDAO.salva(aluno).intValue();

            String numeroFixo = campoTelefoneFixo.getText().toString();
            String numeroCelular = campoTelefoneCelular.getText().toString();
            Telefone telefoneFixo = new Telefone(numeroFixo, TipoTelefone.FIXO, alunoId);
            Telefone telefoneCelular = new Telefone(numeroCelular, TipoTelefone.CELULAR, alunoId);
            telefoneDAO.salva(telefoneFixo, telefoneCelular);
        }
        finish();
    }

    private void inicializacaoDosCampos() {
        campoNome = findViewById(R.id.activity_formulario_aluno_nome);
        campoTelefoneFixo = findViewById(R.id.activity_formulario_aluno_telefone_fixo);
        campoTelefoneCelular = findViewById(R.id.activity_formulario_aluno_telefone_celular);
        campoEmail = findViewById(R.id.activity_formulario_aluno_email);
    }

    private void preencheAluno() {
        String nome = campoNome.getText().toString();
        String telefoneFixo = campoTelefoneFixo.getText().toString();
        String telefoneCelular = campoTelefoneCelular.getText().toString();

        String email = campoEmail.getText().toString();

        aluno.setNome(nome);
        //aluno.setTelefoneFixo(telefoneFixo);
        //aluno.setTelefoneCelular(telefoneCelular);
        aluno.setEmail(email);
    }
}
