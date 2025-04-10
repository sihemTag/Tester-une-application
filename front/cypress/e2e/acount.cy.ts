describe('Login spec', () => {
    beforeEach(() => {
      cy.visit('/login');

      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'yoga@studio.com',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: true
        },
      });

      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

      cy.intercept('GET', '/api/user/1', {
        body: {
          id: 1,
          username: 'yoga@studio.com',
          firstName: 'firstName',
          lastName: 'lastName',
          createdAt: '01/01/2025',
          admin: true
        },
      })
    });
  
    it('Afficher les infos de l\'utilisateur correctement', () => {
      cy.get('.link').contains('Account').should('be.visible').click();

      cy.contains('User information').should('be.visible');
      cy.contains('Name: firstName LASTNAME').should('be.visible');
      cy.contains('Create at:').should('be.visible');
      cy.contains('Last update:').should('be.visible');
    });

    it('Should navigate back when clicking the back button', () => {
      cy.get('.link').contains('Account').click();
      cy.get('button[mat-icon-button]').click();
      cy.url().should('include', '/sessions');
    });
}
)