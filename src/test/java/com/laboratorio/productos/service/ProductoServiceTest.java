package com.laboratorio.productos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.laboratorio.productos.model.Producto;
import com.laboratorio.productos.repository.ProductoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias de ProductoService")
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @Test
    @DisplayName("findAll devuelve la lista simulada por el repositorio")
    void findAll_debeRetornarLaListaRecuperadaDelRepositorio() {
        Producto laptop = new Producto(1L, "Laptop", new BigDecimal("1500.00"), 5);
        Producto mouse = new Producto(2L, "Mouse", new BigDecimal("25.50"), 20);
        when(productoRepository.findAll()).thenReturn(List.of(laptop, mouse));

        List<Producto> resultado = productoService.findAll();

        assertThat(resultado)
                .hasSize(2)
                .containsExactly(laptop, mouse);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Laptop");
        assertThat(resultado.get(0).getPrecio()).isEqualByComparingTo("1500.00");
        assertThat(resultado.get(0).getStock()).isEqualTo(5);
        assertThat(resultado.get(1).getNombre()).isEqualTo("Mouse");
        assertThat(resultado.get(1).getPrecio()).isEqualByComparingTo("25.50");
        assertThat(resultado.get(1).getStock()).isEqualTo(20);
        verify(productoRepository).findAll();
    }

    @Test
    @DisplayName("create persiste el producto y conserva sus atributos")
    void create_debePersistirElProductoYVerificarSusAtributos() {
        Producto nuevo = new Producto(null, "Teclado", new BigDecimal("89.99"), 10);
        when(productoRepository.save(nuevo)).thenAnswer(invocation -> {
            Producto recibido = invocation.getArgument(0);
            return new Producto(3L, recibido.getNombre(), recibido.getPrecio(), recibido.getStock());
        });

        Producto resultado = productoService.create(nuevo);

        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository).save(captor.capture());

        Producto persistido = captor.getValue();
        assertThat(persistido.getNombre()).isEqualTo("Teclado");
        assertThat(persistido.getPrecio()).isEqualByComparingTo(new BigDecimal("89.99"));
        assertThat(persistido.getStock()).isEqualTo(10);

        assertThat(resultado.getId()).isEqualTo(3L);
        assertThat(resultado.getNombre()).isEqualTo("Teclado");
        assertThat(resultado.getPrecio()).isEqualByComparingTo("89.99");
        assertThat(resultado.getStock()).isEqualTo(10);
    }
}
