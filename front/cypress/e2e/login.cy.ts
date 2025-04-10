describe('Login spec', () => {
  beforeEach(() => {
    cy.visit('/login');
  });

  it('Login successfull', () => {
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  });

  it('Should show validation errors when fields are invalid', () => {
    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName="email"]').type('invalid-email');
    cy.get('input[formControlName="password"]').type('12');

    cy.get('button[type="submit"]').should('be.disabled');
  });

  it('Should show error when required fields are missing', () => {
    // Vérifier que le bouton est désactivé si aucun champ n'est rempli
    cy.get('button[type="submit"]').should('be.disabled');
  
    // Remplir uniquement l'email et laisser le mot de passe vide
    cy.get('input[formControlName="email"]').type('yoga@studio.com');
  
    // Vérifier que le bouton est toujours désactivé
    cy.get('button[type="submit"]').should('be.disabled');
  
    // Effacer l'email et remplir uniquement le mot de passe
    cy.get('input[formControlName="email"]').clear();
    cy.get('input[formControlName="password"]').type('test!1234');
  
    // Vérifier que le bouton est toujours désactivé
    cy.get('button[type="submit"]').should('be.disabled');
  });

  
});