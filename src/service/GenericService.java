package service;

import java.util.List;

public interface GenericService<T> {  
  
    T insertar(T entity) throws Exception;  
  
    T actualizar(T entity) throws Exception;  
  
    void eliminar(Long id) throws Exception;  // baja lógica  
  
    T getById(Long id) throws Exception;  
  
    List<T> getAll() throws Exception;  
}
