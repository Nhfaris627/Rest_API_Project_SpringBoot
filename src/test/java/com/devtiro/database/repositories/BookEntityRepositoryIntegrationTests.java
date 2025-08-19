package com.devtiro.database.repositories;

import com.devtiro.database.TestDataUtil;
import com.devtiro.database.domain.entities.AuthorEntity;
import com.devtiro.database.domain.entities.BookEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class BookEntityRepositoryIntegrationTests {

    @Autowired
    private BookRepository underTest;

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void testThatBookCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);

        BookEntity bookEntity = TestDataUtil.createTestBookEntityA(savedAuthor);
        underTest.save(bookEntity);

        Optional<BookEntity> result = underTest.findById(bookEntity.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get().getIsbn()).isEqualTo(bookEntity.getIsbn());
        assertThat(result.get().getTitle()).isEqualTo(bookEntity.getTitle());
        assertThat(result.get().getAuthorEntity().getName()).isEqualTo(savedAuthor.getName());
    }

    @Test
    public void testThatMultipleBooksCanBeCreatedAndRecalled() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(savedAuthor);
        underTest.save(bookEntityA);

        BookEntity bookEntityB = TestDataUtil.createTestBookB(savedAuthor);
        underTest.save(bookEntityB);

        BookEntity bookEntityC = TestDataUtil.createTestBookC(savedAuthor);
        underTest.save(bookEntityC);

        Iterable<BookEntity> result = underTest.findAll();
        assertThat(result).hasSize(3);

        // Check by ISBN instead of exact object equality
        assertThat(result).extracting("isbn")
                .containsExactlyInAnyOrder(
                        "978-1-2345-6789-0",
                        "978-1-2345-6789-1",
                        "978-1-2345-6789-2"
                );
    }

    @Test
    public void testThatBookCanBeUpdated() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(savedAuthor);
        BookEntity savedBook = underTest.save(bookEntityA);

        savedBook.setTitle("UPDATED");
        underTest.save(savedBook);

        Optional<BookEntity> result = underTest.findById(savedBook.getIsbn());
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("UPDATED");
    }

    @Test
    public void testThatBookCanBeDeleted() {
        AuthorEntity authorEntity = TestDataUtil.createTestAuthorEntityA();
        AuthorEntity savedAuthor = authorRepository.save(authorEntity);

        BookEntity bookEntityA = TestDataUtil.createTestBookEntityA(savedAuthor);
        BookEntity savedBook = underTest.save(bookEntityA);

        underTest.deleteById(savedBook.getIsbn());

        Optional<BookEntity> result = underTest.findById(savedBook.getIsbn());
        assertThat(result).isEmpty();
    }
}