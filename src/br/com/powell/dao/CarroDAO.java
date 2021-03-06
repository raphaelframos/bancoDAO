package br.com.powell.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import br.com.powell.model.Carro;
import br.com.powell.utils.ConstantsUtils;

public class CarroDAO extends AbstractDAO{

	public CarroDAO(Context context) {
		super(context);
	}

	public Long adiciona(Carro carro) {
		if(existe(carro.getId())){
			Log.i("CarroDao", "atualiza");
			return atualiza(carro);
		}else{
			Log.i("CarroDao", "insere");
			return insere(carro);
		}
	}

	private Long atualiza(Carro carro) {
		ContentValues valores = montaValues(carro);
		return (long) retornaBancoParaLeitura().update(ConstantsUtils.TABELA_CARRO, valores, ConstantsUtils.CARRO_ID + " =?", new String[] {carro.getId().toString()});
	}

	private long insere(Carro carro) {
		ContentValues valores = montaValues(carro);
		return retornaBancoParaEscrita().insert(ConstantsUtils.TABELA_CARRO, null, valores);
	}

	private ContentValues montaValues(Carro carro) {
		ContentValues valores = new ContentValues();
		valores.put(ConstantsUtils.CARRO_ID, carro.getId());
		valores.put(ConstantsUtils.CARRO_DESCRICAO, carro.getDescricao());
		valores.put(ConstantsUtils.CARRO_COR, carro.getCor());
		return valores;
	}

	private boolean existe(Long id) {

		try{
			Cursor cursorComIdCarro = retornaBancoParaLeitura().query(ConstantsUtils.TABELA_CARRO, new String[] 
					{ConstantsUtils.CARRO_ID}, ConstantsUtils.CARRO_ID + " =?", new String[] {id.toString()}, 
					null, null, null);
			boolean existeCarro = cursorComIdCarro.getCount() > 0;
			fechaCursor(cursorComIdCarro);
			return existeCarro;
		}catch(Exception e){
			return false;
		}
	}

	public ArrayList<Carro> retornaTodosOsCarros() {

		Cursor cursorComTodosOsCarros = retornaBancoParaLeitura().query(ConstantsUtils.TABELA_CARRO, null, null, null, 
				null, null, null);
		cursorComTodosOsCarros.moveToFirst();
		ArrayList<Carro> carros = new ArrayList<Carro>();
		
		try{
			do{
				carros.add(montaCarroPelo(cursorComTodosOsCarros));
			}while(cursorComTodosOsCarros.moveToNext());
		}catch(Exception e){
			carros = new ArrayList<Carro>();
		}
		fechaCursor(cursorComTodosOsCarros);
		return carros;
	}

	private Carro montaCarroPelo(Cursor cursorComTodosOsCarros) {

		Carro carro = new Carro();
		carro.setCor(cursorComTodosOsCarros.getString(cursorComTodosOsCarros.getColumnIndex(ConstantsUtils.CARRO_COR)));
		carro.setDescricao(cursorComTodosOsCarros.getString(cursorComTodosOsCarros.getColumnIndex(ConstantsUtils.CARRO_DESCRICAO)));
		carro.setId(cursorComTodosOsCarros.getLong(cursorComTodosOsCarros.getColumnIndex(ConstantsUtils.CARRO_ID)));
		return carro;
	}

}
